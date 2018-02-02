package com.securespaces.wizard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.securespaces.android.spaceapplibrary.SsrmQueryParams;


/**
 * Created by eric on 26/07/16.
 */
public class FragmentRecommendedApps extends BootstrapFragment implements IWebErrorHandler {
    // Fragment ID set in the restrictions when assigning which fragments to show
    public static final String ID = "fragment_recommended_apps";

    Button mLaunchButton;
    Context mContext;
    Toolbar mToolbar;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_apps, container, false);
        mView = view;
        mContext = getActivity();
        setReturnListeners(view);
        configureToolbar(view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbarTitle.setText(R.string.recommended_apps_title1);

        mLaunchButton = (Button)view.findViewById(R.id.proceedButton);

        configureToolbarTitle();
        configureButton();

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
        webView.loadUrl(getUrl());

        return view;
    }

    private String getUrl() {
        String url = ConfigurationManager.getInstance().getData(ConfigurationManager.PAGE_2_BACKGROUND_URL);
        if (url != null) {
            String params = (new SsrmQueryParams(mContext)).toString();
            if (!params.isEmpty()) {
                url += params;
            }
        }
        return url;
    }

    private void configureButton() {
        String buttonText = ConfigurationManager.getInstance().getData(ConfigurationManager.BUTTON_TEXT_2);
        if (buttonText != null) {
            mLaunchButton.setText(buttonText);
        }
        String action = ConfigurationManager.getInstance().getData(ConfigurationManager.BUTTON_ACTION_2);
        if (ConfigurationManager.ACTION_BUTTON_CLOSE.equals(action)) {
            mLaunchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BootstrapController.getInstance().onLaunchPressed();
                }
            });
        } else if (action == null || ConfigurationManager.ACTION_BUTTON_NEXT_PAGE.equals(action)) {
            mLaunchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BootstrapController.getInstance().onProceed();
                }
            });
        }

        try {
            int buttonTextColor = Color.parseColor(ConfigurationManager.getInstance().getData(ConfigurationManager.BUTTON_TEXT_COLOR_2));
            mLaunchButton.setTextColor(buttonTextColor);
        } catch (NullPointerException | IllegalArgumentException ex) {
            // no/invalid color was set so we'll stick with the default
        }

        try {
            int buttonColor = Color.parseColor(ConfigurationManager.getInstance().getData(ConfigurationManager.BUTTON_COLOR_2));
            mLaunchButton.setBackgroundColor(buttonColor);
        } catch (NullPointerException | IllegalArgumentException ex) {
            // no/invalid color was set so we'll stick with the default
        }
    }

    private void configureToolbarTitle() {
        String toolbarTitle = ConfigurationManager.getInstance().getData(ConfigurationManager.PAGE_2_HEADER_TEXT);
        if (toolbarTitle != null) {
            mToolbarTitle.setText(toolbarTitle);
        }
    }

    @Override
    public void handleWebConnectionError() {

    }
}
