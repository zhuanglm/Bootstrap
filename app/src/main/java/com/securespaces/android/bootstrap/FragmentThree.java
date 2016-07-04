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

        ImageView backButton = (ImageView) mView.findViewById(R.id.backImage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView toolbarTitle = (TextView)mView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.fragment3_title);

        return mView;
    }

    public void targetApkFound() {
        if (mTopTextView != null)
            mTopTextView.setText(R.string.space_ready);
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        if (mSwitchInstructionsTextView != null)
            mSwitchInstructionsTextView.setVisibility(View.VISIBLE);
        if (mSwitchIcon != null)
            mSwitchIcon.setVisibility(View.VISIBLE);
    }
}
