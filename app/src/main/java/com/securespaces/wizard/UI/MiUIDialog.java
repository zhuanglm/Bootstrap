package com.securespaces.wizard.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.securespaces.android.spaceapplibrary.dialog.OEMDialog;
import com.securespaces.wizard.R;


/**
 * Created by raymond on 12/6/16.
 */

public class MiUIDialog extends OEMDialog {
    private int mMsgRows =0 ;
    private int mMaxRows =1 ;
    protected Button mCustomButton;

    private ImageView mImageView;

    public MiUIDialog(Activity activity) {
        super(activity,com.securespaces.android.spaceapplibrary.R.style.XiaomiDialog);
        this.setContentView(R.layout.dialog_mi);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = -1;
        lp.height = -2;
        lp.gravity = 81;
        window.setAttributes(lp);
        this.mTitle = (TextView)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.title);
        this.mMessage = (TextView)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.message);
        this.mCancelButton = (Button)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.cancelButton);
        this.mContinueButton = (Button)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.continueButton);
        mCustomButton = (Button)this.findViewById(R.id.customButton);
        this.mDividerView = this.findViewById(com.securespaces.android.spaceapplibrary.R.id.buttonDivider);
        this.mCentralView = (FrameLayout)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.centralView);
        this.mImageView = (ImageView)this.findViewById(com.securespaces.android.spaceapplibrary.R.id.imageView);
        this.mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MiUIDialog.this.dismiss();
            }
        });
        this.mContinueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MiUIDialog.this.dismiss();
            }
        });
        Typeface face = getXiaomiRegularTypeface(this.mActivity);
        this.mTitle.setTypeface(getXiaomiBoldTypeface(this.mActivity));
        this.mMessage.setTypeface(face);
        this.mContinueButton.setTypeface(face);
        this.mCancelButton.setTypeface(face);
    }

    public void setMaxRows(int n) {
        mMaxRows = n;

        if(this.mMessage != null) {
            for(int i=mMsgRows;i<n;i++)
                appendMessage("\n");
        }
    }

    public void appendMessage(String message) {
        if(this.mMessage != null) {
            String currentMsg = getMessage();
            currentMsg += message;
            mMsgRows++;
            if(mMsgRows>mMaxRows)
                currentMsg = delMsgRow(currentMsg);
            this.mMessage.setText(currentMsg);
        }

    }

    @Override
    public void setMessage(int resourceId) {
        super.setMessage(resourceId);
        String currentMsg = getMessage();
        if(currentMsg.indexOf("\n") != -1)
        {
            String[] str = currentMsg.split("\n");
            mMsgRows += str.length;
        }
        else
            mMsgRows++;
    }

    public String getMessage() {
        if(this.mMessage != null) {
            return String.valueOf(mMessage.getText());
        }
        else
            return null;
    }

    public void disablePositiveButton() {
        this.mContinueButton.setEnabled(false);
    }

    public void visibleCustomButton() {
        mCustomButton.setVisibility(View.VISIBLE);
    }

    public void setCustomButton(View.OnClickListener listener) {
        mCustomButton.setOnClickListener(listener);
    }

    private String delMsgRow(String msg) {
        int pos1 = msg.indexOf("\n");
        String firstRow,restPart;
        if (pos1 >= 0)
            firstRow = msg.substring(0, pos1 + 2);  //keep first row and second \n
        else
            return null;

        int pos2 = msg.indexOf("\n",pos1+2);
        if (pos2 >= 0) {
            restPart = msg.substring(pos2+1);
            msg = firstRow + restPart;
            mMsgRows--;
        } else
            msg = "";
        return msg;
    }

    public static Typeface getXiaomiRegularTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Miui-Regular.ttf");
    }

    public static Typeface getXiaomiBoldTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Miui-Bold.ttf");
    }

    @Override
    public void setSpaceNotReady() {

    }

    @Override
    public void setSpaceCreationDisabled() {

    }

    @Override
    public void setWrongSpace() {

    }

    @Override
    public void setNetworkNotAvailable() {

    }

    @Override
    public void setSpacesNotAvailable() {

    }

    @Override
    public void setSpacesNotEnabled() {

    }

    @Override
    public void setMaxSpacesReached() {

    }

}
