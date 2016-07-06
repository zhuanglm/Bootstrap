package com.securespaces.android.bootstrap;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class FragmentThree extends Fragment {
    private static final int STATE_PACKAGE_NOT_FOUND = 0;
    private static final int STATE_PACKAGE_FOUND = 1;

    private ProgressBar mProgressBar;
    private int mState;
    private FrameLayout mContainer;
    TextView mToolbarTitle;
    Button mProceedButton;

    public View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        mToolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        mProceedButton = (Button)view.findViewById(R.id.proceedButton);
        mContainer = (FrameLayout)view.findViewById(R.id.subContainer);

        if (!BootstrapActivity.canFindTargetPackage(getActivity())) {
            mState = STATE_PACKAGE_NOT_FOUND;
            View subView = inflater.inflate(R.layout.subfragment1, container, false);
            mContainer.addView(subView);

            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);

            mToolbarTitle.setText(R.string.fragment3_title);

            mProceedButton.setEnabled(false);
        } else {
            setupSpaceReadyView();
        }

        ImageView backButton = (ImageView) view.findViewById(R.id.backImage);
        backButton.setOnClickListener(mBackListener);
        mToolbarTitle.setOnClickListener(mBackListener);

        return view;
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
