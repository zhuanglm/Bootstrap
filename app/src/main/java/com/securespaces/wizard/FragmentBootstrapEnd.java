package com.securespaces.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentBootstrapEnd extends BootstrapFragment {
    // Fragment ID set in the restrictions when assigning which fragments to show
    public static final String ID = "fragment_end";

    private Button mStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end, null);

        setReturnListeners(view);
        configureToolbar(view);

        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.end_toolbar_title);

        mStartButton = (Button) view.findViewById(R.id.continueButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BootstrapController.getInstance().onLaunchPressed();
            }
        });

        return view;
    }
}
