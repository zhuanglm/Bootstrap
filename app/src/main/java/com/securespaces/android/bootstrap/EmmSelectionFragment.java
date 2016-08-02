package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by eric on 02/08/16.
 */
public class EmmSelectionFragment extends BootstrapFragment {

    public static EmmSelectionFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BootstrapFragment.KEY_POSITION, position);
        EmmSelectionFragment fragment = new EmmSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emm_selection, null);

        unpackBundle();
        setReturnListeners(view);

        mToolbarTitle.setText(R.string.emm_select_title);

        Button skipButton = (Button)view.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceed();
            }
        });

        Button nationskyButton = (Button)view.findViewById(R.id.nationskyButton);
        nationskyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_NATIONSKY);
            }
        });

        Button thundersoftButton = (Button)view.findViewById(R.id.thundersoftButton);
        thundersoftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_THUNDERSOFT);
            }
        });

        return view;
    }
}
