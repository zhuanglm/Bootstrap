package com.securespaces.wizard;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by eric on 02/08/16.
 */
public class BootstrapFragment extends Fragment {
    TextView mToolbarTitle;
    ImageView mBackButton;

    public View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BootstrapController.getInstance().onBack();
        }
    };

    protected BootstrapActivity getBootstrapActivity() {
        return (BootstrapActivity)getActivity();
    }

    protected void setReturnListeners(View view) {
        mBackButton = (ImageView) view.findViewById(R.id.backImage);
        mBackButton.setOnClickListener(mBackListener);

        mToolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setOnClickListener(mBackListener);
    }
}
