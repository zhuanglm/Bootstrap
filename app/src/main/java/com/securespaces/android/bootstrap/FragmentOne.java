package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.TextView;

public class FragmentOne extends InsetFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one, container, false);

        TextView toolbarTitle = (TextView)mView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.space_name);

        Button proceedButton = (Button)mView.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BootstrapActivity)getActivity()).switchFragment(0);
            }
        });

        return mView;
    }

}
