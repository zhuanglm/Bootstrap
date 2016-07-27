package com.securespaces.android.bootstrap;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by eric on 26/07/16.
 */
public class RecommendedAppsFragment extends Fragment implements IWebErrorHandler {

    private static final String SS_URL_PARTIAL_PREFIX1 = "securespaces.com/generateSpace";
    private static final String SS_URL_PARTIAL_PREFIX2 = "securespaces.com/registerSpace";

    public View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_apps, container, false);

        ImageView backButton = (ImageView) view.findViewById(R.id.backImage);
        backButton.setOnClickListener(mBackListener);

        TextView toolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.recommended_apps_title1);
        toolbarTitle.setOnClickListener(mBackListener);

        Button proceedButton = (Button)view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BootstrapActivity)getActivity()).onProceedPushed(1);
            }
        });

        RecommendedAppsWebViewClient webViewClient = new RecommendedAppsWebViewClient(getActivity(), R.integer.grid_view_cols, R.integer.grid_view_rows, RecommendedAppsWebViewClient.TYPE_MORE_SPACES, this);
        WebView webView = (WebView)view.findViewById(R.id.web_view);
        webView.setWebViewClient(webViewClient);


        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {

                WebView newWebView = new WebView(view.getContext());
                newWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (!TextUtils.isEmpty(url)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            /*)
                            if (url.contains(SS_URL_PARTIAL_PREFIX1)
                                    || url.contains(SS_URL_PARTIAL_PREFIX2)) {

                                // we will handle creating our space because we don't want
                                // to launch the Navigator activity
                                onDownloadSpace(url);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                            */
                        }
                        return true;
                    }
                });
                return true;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.loadUrl(webViewClient.getRecommendedAppsUrl());

        return view;
    }

    @Override
    public void handleWebConnectionError() {

    }
}
