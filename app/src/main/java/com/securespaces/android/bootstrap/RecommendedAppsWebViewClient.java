package com.securespaces.android.bootstrap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.securespaces.android.ssm.SystemUtils;

/**
 * Created by eric on 26/07/16.
 */
public class RecommendedAppsWebViewClient extends WebViewClient {
    public static final String TYPE_FEATURED_SPACES = "featured";
    public static final String TYPE_MORE_SPACES = "all";


    private static final String RECOMMENDED_URL_OVERWRITE_PROPERTY = "persist.sys.ssrm_url";
    private static final String RECOMMENDED_URL_API = "/api/v2/device/spaces/recommended?layout=grid";
    private static final String DEFAULT_SSRM_SERVER = "ssrm.securespaces.com";
    private static final String SSRM_SERVER_OVERWRITE_PROPERTY = "persist.sys.ssrm_server";
    private static final String PARAM_COLUMNS = "cols=";
    private static final String PARAM_ROWS = "rows=";
    private static final String PARAM_VIEW = "view=";

    private int mColumns = 0;
    private int mRows = 0;
    private String mViewType = TYPE_MORE_SPACES;
    private Context mContext;
    private Activity mActivity;
    private IWebErrorHandler mErrorHandler;

    public RecommendedAppsWebViewClient(Activity activity, int columns, int rows, String viewType,
                                        IWebErrorHandler errorHandler) {
        mContext = activity;
        mActivity = activity;
        mColumns = columns;
        mRows = rows;
        mViewType = viewType;
        mErrorHandler = errorHandler;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        // FIXME Should actually check the error code, and present a solution to the user if one
        // is available (i.e. network is not available)
        if (errorCode == ERROR_HOST_LOOKUP) {
            if (mErrorHandler != null) {
                mErrorHandler.handleWebConnectionError();
            }
        } else {
            view.setVisibility(View.INVISIBLE);
        }
//        Toast.makeText(mContext, "Error: " + description + ", Error Code: " + errorCode, Toast.LENGTH_LONG).show();
    }

    public String getRecommendedAppsUrl() {
        SystemUtils systemUtils = new SystemUtils();
        String url = systemUtils.getSystemProperty(RECOMMENDED_URL_OVERWRITE_PROPERTY,"");
        if ((url == null) || url.isEmpty()) {
            String ssrmHost = systemUtils.getSystemProperty(SSRM_SERVER_OVERWRITE_PROPERTY,"");
            if ((ssrmHost == null) || ssrmHost.isEmpty()) {
                ssrmHost = DEFAULT_SSRM_SERVER;
            }
            String params = (new SsrmQueryParams(mContext)).toString();
            url = "https://" + ssrmHost + RECOMMENDED_URL_API;
            url += "&" + PARAM_COLUMNS + mColumns;
            url += "&" + PARAM_ROWS + mRows;
            url += "&" + PARAM_VIEW + mViewType;
            if (!params.isEmpty()) {
                url += params;
            }
        }
        return url;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        UrlCache cache = new UrlCache(mActivity);
        WebResourceResponse response = cache.load(request.getUrl().toString());
        //If network and no cache available resume as normal getting from internet
        if (response == null) {
            return null;
        }
        //Return response obtained from cache
        return response;
    }
}
