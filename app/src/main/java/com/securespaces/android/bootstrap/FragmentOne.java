package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentOne extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        TextView toolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.fragment1_title);

        Button proceedButton = (Button)view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BootstrapActivity)getActivity()).onProceedPushed(0);
            }
        });

        return view;
    }

}
