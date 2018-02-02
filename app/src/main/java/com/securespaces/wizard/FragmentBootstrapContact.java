package com.securespaces.wizard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.securespaces.wizard.server.PutInfoTask;
import com.securespaces.wizard.server.UserInfo;

public class FragmentBootstrapContact extends BootstrapFragment implements TextWatcher{
    // Fragment ID set in the restrictions when assigning which fragments to show
    public static final String ID = "fragment_contact";

    private Button mSendButton;
    private Button mContinueButton;
    private EditText mName;
    private FormEditText mEmail;
    private EditText mCompanyName;
    private EditText mWeChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, null);

        setReturnListeners(view);
        configureToolbar(view);

        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.contact_bar);

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = getResources().getConfiguration();
                UserInfo userInfo = new UserInfo(config);
                if (saveInfo(view,userInfo)) {
                    PutInfoTask.putInfoTask(getContext(), userInfo);
                    onProceed(true);
                }
            }
        });

        mContinueButton = (Button) view.findViewById(R.id.notNowButton);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceed(true);
            }
        });

        mName = ((EditText) view.findViewById(R.id.editName));
        mEmail = ((FormEditText) view.findViewById(R.id.editEMail));
        mCompanyName = ((EditText) view.findViewById(R.id.editCompany));
        mWeChat = ((EditText) view.findViewById(R.id.editWeChat));

        mName.addTextChangedListener(this);
        mEmail.addTextChangedListener(this);
        mCompanyName.addTextChangedListener(this);
        mWeChat.addTextChangedListener(this);

        return view;
    }

    private boolean saveInfo(View view, UserInfo userInfo) {
        userInfo.fullName = mName.getText().length() > 0 ? mName.getText().toString() : null;
        userInfo.email = mEmail.getText().length() > 0 ? mEmail.getText().toString() : null;
        if (userInfo.email != null)
            if (!mEmail.testValidity()) {
                mEmail.requestFocus();
                return false;
            }
        userInfo.company = mCompanyName.getText().length() > 0 ? mCompanyName.getText().toString() : null;

        userInfo.additionalNotes = null;
        //String isRecommend = ((RadioButton) view.findViewById(R.id.rBYes)).isChecked() ? "true" : "false";

        if (mWeChat.getText().length() > 0) {
            ArrayMap<String, String> map = new ArrayMap<>();
            String wx = mWeChat.getText().toString();
            map.put("type", "WeChat");
            map.put("value", wx);
            userInfo.appIds.add(map);
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtils.isEmpty(mName.getText()) && TextUtils.isEmpty(mEmail.getText())
                && TextUtils.isEmpty(mCompanyName.getText()) && TextUtils.isEmpty(mWeChat.getText())) {
            mSendButton.setEnabled(false);
        } else {
            mSendButton.setEnabled(true);
        }
    }
}


