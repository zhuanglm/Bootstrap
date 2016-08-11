package com.securespaces.android.bootstrap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by eric on 02/08/16.
 */
public class EmmSelectionFragment extends BootstrapFragment {
    Button mNationskyButton, mThundersoftButton, mSkipButton, mNoEmmButton;

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

        mToolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.emm_select_title);

        mSkipButton = (Button)view.findViewById(R.id.skipButton);
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceed();
            }
        });

        mNoEmmButton = (Button)view.findViewById(R.id.continueButton);
        mNoEmmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceed();
            }
        });

        mNationskyButton = (Button)view.findViewById(R.id.nationskyButton);
        mNationskyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_NATIONSKY);
                onProceed();
            }
        });

        mThundersoftButton = (Button)view.findViewById(R.id.thundersoftButton);
        mThundersoftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_THUNDERSOFT);
                onProceed();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!prefs.getString(BootstrapActivity.KEY_CHOSEN_EMM, "").equals("")) {
            emmAlreadyChosen();
        }

        return view;
    }

    private void emmAlreadyChosen() {
        mSkipButton.setText(R.string.button_continue);
        mNationskyButton.setEnabled(false);
        mThundersoftButton.setEnabled(false);
    }
}
