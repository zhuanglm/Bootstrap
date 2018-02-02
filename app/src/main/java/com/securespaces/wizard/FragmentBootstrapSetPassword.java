package com.securespaces.wizard;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.securespaces.wizard.R;

public class FragmentBootstrapSetPassword extends BootstrapFragment {
    // Fragment ID set in the restrictions when assigning which fragments to show
    public static final String ID = "fragment_password";

    private Button mSetButton;
    private Button mContinueButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, null);

        setReturnListeners(view);
        configureToolbar(view);

        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.password_bar);

        mSetButton = (Button) view.findViewById(R.id.setPasswordButton);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("/");
//                ComponentName cm = new ComponentName("com.android.settings","com.android.settings.ChooseLockGeneric");
//                intent.setComponent(cm);
                Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
            }
        });

        mContinueButton = (Button) view.findViewById(R.id.notNowButton);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getBootstrapActivity().onEmmChosen(BootstrapActivity.EMM_NONE);
                onProceed(true);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                return;
            }
            onProceed(false);
        } else
            Toast.makeText(getActivity(), R.string.password_set_later, Toast.LENGTH_SHORT).show();
    }

//    private void emmAlreadyChosen() {
//        mStartButton.setText(R.string.button_continue);
//    }
}


