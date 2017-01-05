package com.securespaces.fashion.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.securespaces.fashion.wizard.R;

/**
 * Created by eric on 02/08/16.
 */
public class EmmSelectionFragment extends BootstrapFragment {
    Button mNationskyButton, mNoEmmButton;

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

        mNoEmmButton = (Button)view.findViewById(R.id.continueButton);
        mNoEmmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_NONE);
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

        return view;
    }

    private void emmAlreadyChosen() {
        mNationskyButton.setEnabled(false);
        mNoEmmButton.setText(R.string.button_continue);
    }
}
