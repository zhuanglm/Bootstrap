package com.securespaces.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;
import com.securespaces.wizard.UI.MiUIDialog;

public class FragmentBootstrapSetSecurity extends BootstrapFragment implements View.OnClickListener {
    // Fragment ID set in the restrictions when assigning which fragments to show
    public static final String ID = "fragment_security";

    private SpacesManager mSpacesManager;
    private FragmentBootstrapSetSecurity mFragment;
    private BootstrapActivity mActivity;
    OnUpdateTaskListener mTaskListener;

    private static final String TAG = FragmentBootstrapSetSecurity.class.getSimpleName();

    static final int SPACE_DETAILS_REQUEST = 10001;
    static final String EXTRA_SPACE_ACTION = "extra_space_action";
    static final String EXTRA_SPACE_ID = "extra_space_id";
    static final String SPACE_ACTION_UPDATED = "space_action_updated";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BootstrapActivity) getActivity();
        mSpacesManager = new SpacesManager(mActivity);
        mFragment = this;
//        mTaskListener = mActivity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_security, null);

        setReturnListeners(view);
        configureToolbar(view);

        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.security_bar);

        view.findViewById(R.id.lowLevelButton).setOnClickListener(this);
        view.findViewById(R.id.mediumLevelButton).setOnClickListener(this);
        view.findViewById(R.id.highLevelButton).setOnClickListener(this);
        view.findViewById(R.id.customButton).setOnClickListener(this);
        view.findViewById(R.id.imageView_low).setOnClickListener(this);
        view.findViewById(R.id.imageView_medium).setOnClickListener(this);
        view.findViewById(R.id.imageView_high).setOnClickListener(this);

        return view;
    }

    private void launchSpaceDetails() {
        int spaceId = mSpacesManager.getSpace(mSpacesManager.getCurrentSpace()).id;
        Intent detailsIntent = mSpacesManager.getSpaceDetailsIntent(UserUtils.getUserHandleFor(spaceId));
        startActivityForResult(detailsIntent, SPACE_DETAILS_REQUEST);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPACE_DETAILS_REQUEST && resultCode == Activity.RESULT_OK) {
            String action = data.getStringExtra(EXTRA_SPACE_ACTION);
            int spaceId = data.getIntExtra(EXTRA_SPACE_ID, -1);
            if (spaceId != -1 && !TextUtils.isEmpty(action)) {
                if (SPACE_ACTION_UPDATED.equals(action)) {
                    //Toast.makeText(getActivity(), "updated : " + spaceId, Toast.LENGTH_SHORT).show();
                }
            }

            //onProceed(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lowLevelButton:
                SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.low_security_items);
                onProceed(true);

                break;

            case R.id.mediumLevelButton:
                SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.medium_security_items);
                onProceed(true);

                break;

            case R.id.highLevelButton:
                SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.high_security_items);
                onProceed(true);

                break;

            case R.id.customButton:
                launchDialog(R.string.custom_title, R.string.set_security_warning);

                break;

            case R.id.imageView_low:
                launchDialog(R.string.low_title, R.string.low_security);

                break;

            case R.id.imageView_medium:
                launchDialog(R.string.medium_title, R.string.medium_security);

                break;

            case R.id.imageView_high:
                launchDialog(R.string.high_title, R.string.high_security);

                break;
        }

    }

    private void launchDialog(final int titleID, int msgID) {
        final MiUIDialog xiaomiDialog = new MiUIDialog(getActivity());

        xiaomiDialog.setTitle(titleID);
        xiaomiDialog.setMessage(msgID);

        if (titleID == R.string.custom_title) {
            xiaomiDialog.visibleCustomButton();
            xiaomiDialog.setCustomButton(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    launchSpaceDetails();
                }
            });
        }
        else
            xiaomiDialog.setMaxRows(2);

        xiaomiDialog.setPositiveButton(R.string.select, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                xiaomiDialog.disablePositiveButton();
                switch (titleID) {
                    case R.string.custom_title:
                        xiaomiDialog.dismiss();
                        onProceed(true);
                        break;

                    case R.string.low_title:
                        SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.low_security_items);
                        xiaomiDialog.dismiss();
                        onProceed(true);
                        break;
                    case R.string.medium_title:
                        SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.medium_security_items);
                        xiaomiDialog.dismiss();
                        onProceed(true);
                        break;
                    case R.string.high_title:
                        SetRestrictionsTask.setRestrictionsTask(mFragment,R.array.high_security_items);
                        xiaomiDialog.dismiss();
                        onProceed(true);
                        break;
                }

            }

        });
        xiaomiDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                xiaomiDialog.dismiss();
            }

        });

        xiaomiDialog.show();
    }

    public void updateButton(boolean flag) {
//        mTaskListener.OnUpdateTaskListener(flag);
    }

    public interface OnUpdateTaskListener {
        void OnUpdateTaskListener(boolean flag);
    }

}
