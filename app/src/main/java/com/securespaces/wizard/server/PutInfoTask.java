package com.securespaces.wizard.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.securespaces.android.ssm.SpaceUtils;
import com.securespaces.android.ssm.SpacesManager;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by raymond on 12/30/16.
 */

public class PutInfoTask extends AsyncTask<Void, String, Void> {
    private static final String TAG = "User Information";
    private UserInfo mUserData;
    private String mServerUrl;
    private String mRegistrationId;
    private int mSpaceId;

    public static void putInfoTask(Context context,UserInfo userData) {
        PutInfoTask task = new PutInfoTask(context,userData);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public PutInfoTask(Context context,UserInfo userData) {
        mUserData = userData;
        SpacesManager spacesManager = new SpacesManager(context);
        mSpaceId = spacesManager.getSpace(spacesManager.getCurrentSpace()).id;
        mServerUrl = SpacesManager.Provider.getString(
                context.getContentResolver(),
                SpacesManager.Provider.SSMS_SERVER_URL,
                null,mSpaceId);
        mRegistrationId = SpacesManager.Provider.getString(
                context.getContentResolver(),
                SpacesManager.Provider.SSMS_REGISTRATION_ID,
                null,mSpaceId);
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mSpaceId == 0)
            return null;

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader(
                        SpaceUtils.X_SECURE_SPACES_SECRET_KEY,
                        SpaceUtils.X_SECURE_SPACES_SECRET_VALUE);
            }
        };

        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
                .setEndpoint(mServerUrl)
                .setRequestInterceptor(requestInterceptor)
                .setErrorHandler(new BackendErrorHandler());

        IBackendService backendService = restAdapterBuilder.build().create(
                IBackendService.class);

        try {
            backendService.putUserInformation(mRegistrationId, mUserData);
        } catch (RetrofitError e) {
            String json = new String(((TypedByteArray) e.getResponse().getBody()).getBytes());
            Log.e(TAG, e.getUrl() + " " + e.getMessage() + " " + json);
        } finally {
            return null;
        }

    }

    @Override
    protected void onPostExecute(Void p) {
        super.onPostExecute(p);
    }

    private class BackendErrorHandler implements ErrorHandler {
        private static final String TAG = "BackendErrorHandler";

        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            if (r != null && r.getStatus() == 400) {
                ErrorResponse errorResponse = (ErrorResponse)cause.getBodyAs(ErrorResponse.class);
                Log.d(TAG, "message = " + errorResponse.msg);
                if (ErrorResponse.SPACE_NOT_IN_REGISTERED_STATE.equals(errorResponse.msg)) {
                    return new Exception(cause);
                }
            }
            return cause;
        }
    }

}
