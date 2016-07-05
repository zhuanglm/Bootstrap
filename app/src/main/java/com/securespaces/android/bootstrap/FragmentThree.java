package com.securespaces.android.bootstrap;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by eric on 15/06/16.
 */
public class FragmentThree extends InsetFragment {
    private static final int STATE_PACKAGE_NOT_FOUND = 0;
    private static final int STATE_PACKAGE_FOUND = 1;

    private ProgressBar mProgressBar;
    BootstrapActivity mActivity;
    private int mState;
    private FrameLayout mContainer;
    TextView mToolbarTitle;
    Button mProceedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_three, container, false);

        mToolbarTitle = (TextView)mView.findViewById(R.id.toolbar_title);
        mProceedButton = (Button)mView.findViewById(R.id.proceedButton);
        mContainer = (FrameLayout)mView.findViewById(R.id.subContainer);

        if (!BootstrapActivity.canFindTargetPackage(getActivity())) {
            mState = STATE_PACKAGE_NOT_FOUND;
            View subView = inflater.inflate(R.layout.subfragment1, container, false);
            mContainer.addView(subView);

            mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
            mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);

            mToolbarTitle.setText(R.string.fragment3_title);
            //mProceedButton.setEnabled(false);
            mProceedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTargetPackageFound();
                }
            });
        } else {
            setupSpaceReadyView();
        }

        ImageView backButton = (ImageView) mView.findViewById(R.id.backImage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return mView;
    }

    private void setupSpaceReadyView() {
        mState = STATE_PACKAGE_FOUND;
        View subView = getActivity().getLayoutInflater().inflate(R.layout.subfragment2, null, false);
        mContainer.addView(subView);
        mToolbarTitle.setText(R.string.fragment4_title);
        mProceedButton.setEnabled(true);
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public void onTargetPackageFound() {
        if (mState == STATE_PACKAGE_NOT_FOUND) {
            mContainer.removeAllViews();
            setupSpaceReadyView();
        }
    }

}
