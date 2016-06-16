package com.securespaces.android.bootstrap;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

/**
 * Created by eric on 16/06/16.
 */
public class InsetFragment extends Fragment {
    protected View mView;
    protected WindowInsets mInsets;


    protected void setupInsetListener() {
        if (mInsets != null) {
            applyWindowInsets(mInsets);
        }

        mView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                return insets;
            }
        });
    }

    public void applyWindowInsets(WindowInsets insets) {
        if (mView != null) {
            mView.dispatchApplyWindowInsets(insets);
        } else {
            mInsets = insets;
        }
    }
}
