package com.securespaces.android.bootstrap;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.securespaces.android.ssm.SystemUtils;

import java.util.Locale;

/**
 * Created by eric on 26/07/16.
 */
public class RecommendedAppsWebViewClient extends WebViewClient {

    private static final String DEFAULT_SSRM_SERVER = "ssrm-dev.securespaces.com";
    private static final String RECOMMENDED_URL_API = "api/v2/device/apps/recommended";

    private Context mContext;
    private Activity mActivity;
    private IWebErrorHandler mErrorHandler;

    public RecommendedAppsWebViewClient(Activity activity, IWebErrorHandler errorHandler) {
        mContext = activity;
        mActivity = activity;
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
    }

    public String getRecommendedAppsUrl() {
        String maker = Build.MANUFACTURER;
        String language = Locale.getDefault().toString();
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        return  Uri.decode(uriBuilder.path(DEFAULT_SSRM_SERVER).
                appendPath(RECOMMENDED_URL_API).
                appendQueryParameter("maker", maker).
                appendQueryParameter("language", language).
                build().toString());
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.endsWith(".apk")) {
            // launch dat service!
            Intent intent = new Intent(mContext, AppInstallService.class);
            intent.putExtra(AppInstallService.EXTRA_DOWNLOAD_URL, url);
            mContext.startService(intent);
        }

        return true;
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
