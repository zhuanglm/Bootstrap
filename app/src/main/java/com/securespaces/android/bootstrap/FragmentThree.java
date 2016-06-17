package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by eric on 15/06/16.
 */
public class FragmentThree extends InsetFragment {
    private TextView mTopTextView, mSwitchInstructionsTextView;
    private ProgressBar mProgressBar;
    private ImageView mSwitchIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_three, container, false);
        setupInsetListener();

        mTopTextView = (TextView)mView.findViewById(R.id.topTextView);
        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        mSwitchInstructionsTextView = (TextView)mView.findViewById(R.id.switchTextView);
        mSwitchIcon = (ImageView)mView.findViewById(R.id.imageView2);

        return mView;
    }

    public void targetApkFound() {
        mTopTextView.setText(R.string.space_ready);
        mProgressBar.setVisibility(View.GONE);
        mSwitchInstructionsTextView.setVisibility(View.VISIBLE);
        mSwitchIcon.setVisibility(View.VISIBLE);
    }
}
