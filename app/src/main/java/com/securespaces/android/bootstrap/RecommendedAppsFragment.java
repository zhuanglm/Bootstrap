package com.securespaces.android.bootstrap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
public class RecommendedAppsFragment extends BootstrapFragment implements IWebErrorHandler, IFinalFragment {
    Button mProceedButton;

    public static RecommendedAppsFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BootstrapFragment.KEY_POSITION, position);
        RecommendedAppsFragment fragment = new RecommendedAppsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_apps, container, false);

        unpackBundle();
        setReturnListeners(view);
        mToolbarTitle.setText(R.string.recommended_apps_title1);

        mProceedButton = (Button)view.findViewById(R.id.proceedButton);
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBootstrapActivity().canFindTargetPackage()) {
                    startActivity(getBootstrapActivity().getTargetPackageLaunchIntent());
                }
                getActivity().finish();
            }
        });

        RecommendedAppsWebViewClient webViewClient = new RecommendedAppsWebViewClient(getActivity(), this);
        WebView webView = (WebView)view.findViewById(R.id.web_view);
        webView.setWebViewClient(webViewClient);

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

        if (getBootstrapActivity().getTargetPackage().equals(BootstrapActivity.EMM_NONE)) {
            mProceedButton.setText(R.string.start);
        } else if (getBootstrapActivity().canFindTargetPackage()) {
            onTargetPackageFound();
        }


        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void param){
                mProceedButton.setEnabled(true);
            }
        }.execute();


        return view;
    }

    @Override
    public void onTargetPackageFound() {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(R.string.recommended_apps_title2);
        }
        if (mProceedButton != null) {
            mProceedButton.setEnabled(true);
        }
    }

    @Override
    public void handleWebConnectionError() {

    }
}
