package com.securespaces.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by eric on 27/01/17.
 */

public class IntroFragmentWebViewClient extends WebViewClient {


    private Activity mActivity;
    private IWebErrorHandler mErrorHandler;

    public IntroFragmentWebViewClient(Activity activity, IWebErrorHandler errorHandler) {
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

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
