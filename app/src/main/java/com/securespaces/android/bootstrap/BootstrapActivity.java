package com.securespaces.android.bootstrap;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class BootstrapActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Bootstrap";
    private static final int FINAL_FRAGMENT = 2;

    // com.securespaces.android.xiaomitest.mi
    // com.nq.mdm

    private ArrayList<InsetFragment> mFragments;
    private Button mProceedButton;
    private int mCurrentPosition;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getData().getEncodedSchemeSpecificPart().equals(getTargetPackage())) {
                if (mProceedButton != null) {
                    targetPackageFound();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                 | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_bootstrap);

        mProceedButton = (Button)findViewById(R.id.proceedButton);
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceedPushed();
            }
        });

        mFragments = new ArrayList<>();
        mFragments.add(new FragmentOne());
        mFragments.add(new FragmentTwo());
        mFragments.add(new FragmentThree());



        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);

        gotoFragment(mFragments.get(0));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
        }
    }

    private String getTargetPackage() {
        return getString(R.string.target_package);
    }
    private void onProceedPushed() {
        if (mCurrentPosition < FINAL_FRAGMENT) {
            switchFragment(mFragments.get(mCurrentPosition + 1));
            mCurrentPosition++;
            //mViewPager.setCurrentItem(mCurrentPosition + 1, true);
        } else if (mCurrentPosition == FINAL_FRAGMENT) {
            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getTargetPackage());
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } catch (ActivityNotFoundException ex) {
                //just close then
                Log.e(TAG,"Unable to launch activity");
            }
            finish();
        }
    }

    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }
    private boolean canFindTargetPackage() {
        return getPackageManager().getLaunchIntentForPackage(getTargetPackage()) != null;
    }

    private void targetPackageFound() {
        mProceedButton.setEnabled(true);
        ((FragmentThree)mFragments.get(FINAL_FRAGMENT)).targetApkFound();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        if (position == FINAL_FRAGMENT) {
            mProceedButton.setText(R.string.launch);
            if (canFindTargetPackage()) {
                targetPackageFound();
            } else {
                mProceedButton.setEnabled(false);
            }
        } else {
            mProceedButton.setText(R.string.next);
            mProceedButton.setEnabled(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
