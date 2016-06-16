package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eric on 14/06/16.
 */
public class FragmentTwo extends InsetFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        setupInsetListener();
        return mView;
    }
}
