package com.securespaces.android.bootstrap;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eric on 02/08/16.
 */
public class BootstrapFragment extends Fragment {
    public static String KEY_POSITION = "key_position";
    int mPosition;
    TextView mToolbarTitle;
    ImageView mBackButton;

    public View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    protected void onProceed() {
        ((BootstrapActivity)getActivity()).onProceedPushed(mPosition);
    }

    protected BootstrapActivity getBootstrapActivity() {
        return (BootstrapActivity)getActivity();
    }
    protected void unpackBundle() {
        mPosition = getArguments().getInt(KEY_POSITION, 0);
    }

    protected void setReturnListeners(View view) {
        mBackButton = (ImageView) view.findViewById(R.id.backImage);
        mBackButton.setOnClickListener(mBackListener);

        mToolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setOnClickListener(mBackListener);
    }
}
