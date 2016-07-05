package com.securespaces.android.bootstrap;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by eric on 15/06/16.
 */
public class FragmentThree extends InsetFragment {
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_three, container, false);
        setupInsetListener();

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar);

        ImageView backButton = (ImageView) mView.findViewById(R.id.backImage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView toolbarTitle = (TextView)mView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.fragment3_title);

        Button proceedButton = (Button)mView.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BootstrapActivity)getActivity()).switchFragment(2);
            }
        });

        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);

        return mView;
    }
}
