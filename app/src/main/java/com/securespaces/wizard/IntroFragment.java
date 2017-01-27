package com.securespaces.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by eric on 26/01/17.
 */

public class IntroFragment extends BootstrapFragment implements IWebErrorHandler{
    protected Button mButton;
    protected Context mContext;
    protected View mView;
    protected int mTextColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, null);
        mView = view;

        mContext = getActivity();

        mButton = (Button) view.findViewById(R.id.button);

        setupButton();
        loadTextFromConfigs();

        WebView webView = (WebView)view.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {


            public void onPageFinished(WebView webView, String url) {
                BootstrapController.getInstance().onPageLoaded();
            }
        });

        WebSettings webSettings = webView.getSettings();

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.loadUrl(getUrl());
        return view;
    }

    private String getUrl() {
        String url = ConfigurationManager.getData(getActivity(), ConfigurationManager.FRONT_PAGE_BACKGROUND_URL);
        if (url == null) {
            url ="https://s3.amazonaws.com/secure-spaces-graphite-software/Graphite_Mobile_Wallpaper4.jpg";
        }
        return url;
    }

    private void loadTextFromConfigs() {
        String colorString = ConfigurationManager.getData(mContext, ConfigurationManager.TEXT_COLOR_PRIMARY);
        if (colorString != null) {
            mTextColor = Color.parseColor(colorString);
        }

        assignTextToTextView(ConfigurationManager.TITLE_LINE_1, R.id.titleLine1);
        assignTextToTextView(ConfigurationManager.TITLE_LINE_2, R.id.titleLine2);
        assignTextToTextView(ConfigurationManager.TAG_LINE, R.id.tagLine);
        assignTextToBulletList(ConfigurationManager.BULLET_1, R.id.bulletLine1, R.id.bullet1, R.id.line1);
        assignTextToBulletList(ConfigurationManager.BULLET_2, R.id.bulletLine2, R.id.bullet2, R.id.line2);
        assignTextToBulletList(ConfigurationManager.BULLET_3, R.id.bulletLine3, R.id.bullet3, R.id.line3);
    }

    private void assignTextToTextView(String configKey, int textViewId) {
        assignTextToBulletList(configKey, textViewId, null, null);
    }

    private void assignTextToBulletList(String configKey, int textViewId, Integer bulletId, Integer layoutId) {
        TextView textView = (TextView) mView.findViewById(textViewId);
        String text = ConfigurationManager.getData(mContext, configKey);
        if (text != null) {
            if (text.isEmpty() && layoutId != null) {
                LinearLayout layout = (LinearLayout)mView.findViewById(layoutId);
                layout.setVisibility(View.GONE);
            } else {
                textView.setText(text);
                textView.setTextColor(mTextColor);
                if (bulletId != null) {
                    TextView bulletView = (TextView) mView.findViewById(bulletId);
                    bulletView.setTextColor(mTextColor);
                }
            }
        }
    }
    private void setupButton() {
        try {
            String buttonColor = ConfigurationManager.getData(mContext, ConfigurationManager.BUTTON_COLOR);
            mButton.setBackgroundColor(Color.parseColor(buttonColor));
        } catch (NullPointerException | IllegalArgumentException ex) {
            // no/invalid color was set so we'll stick with the default
        }
        try {
            int buttonTextColor = Color.parseColor(ConfigurationManager.getData(mContext, ConfigurationManager.BUTTON_TEXT_COLOR));
            mButton.setTextColor(buttonTextColor);
        } catch (NullPointerException | IllegalArgumentException ex) {
            // no/invalid color was set so we'll stick with the default
        }
        String buttonText = ConfigurationManager.getData(mContext, ConfigurationManager.BUTTON_TEXT);
        if (buttonText != null) {
            mButton.setText(buttonText);
        }
        String action = ConfigurationManager.getData(getActivity(), ConfigurationManager.BUTTON_ACTION);
        if (action == null || ConfigurationManager.ACTION_BUTTON_NEXT_PAGE.equals(action)) {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BootstrapController.getInstance().onProceed();
                }
            });
        }
    }

    @Override
    public void handleWebConnectionError() {

    }
}
