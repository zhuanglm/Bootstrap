package com.securespaces.android.bootstrap;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BootstrapActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Bootstrap";
    private static final int FINAL_FRAGMENT = 2;
    private static final String TARGET_PACKAGE = "com.nq.mdm";
    private static final String TARGET_CLASS = "com.android.calculator2.Calculator";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<InsetFragment> mFragments;
    private ArrayList<ImageView> mPageIndicators;
    private Button mProceedButton;
    private int mCurrentPosition;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getData().getEncodedSchemeSpecificPart().equals(TARGET_PACKAGE)) {
                if (mProceedButton != null) {
                    mProceedButton.setEnabled(true);
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

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mViewPager.requestApplyInsets();
        mViewPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                for (InsetFragment fragment : mFragments) {
                    fragment.applyWindowInsets(insets);
                }
                return insets;
            }
        });

        mPageIndicators = new ArrayList<>();
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator1));
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator2));
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator3));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void setPageIndicatorDots(int oldPosition, int newPosition) {
        mPageIndicators.get(newPosition).setImageDrawable(getDrawable(R.drawable.circle_full));
        mPageIndicators.get(oldPosition).setImageDrawable(getDrawable(R.drawable.circle_empty));
    }

    private void onProceedPushed() {
        if (mCurrentPosition < FINAL_FRAGMENT) {
            mViewPager.setCurrentItem(mCurrentPosition + 1, true);
        } else if (mCurrentPosition == FINAL_FRAGMENT) {
            try {
                Intent intent = getPackageManager().getLaunchIntentForPackage(TARGET_PACKAGE);
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

    private boolean canFindTargetPackage() {
        return getPackageManager().getLaunchIntentForPackage(TARGET_PACKAGE) != null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPageIndicatorDots(mCurrentPosition, position);
        mCurrentPosition = position;
        if (position == FINAL_FRAGMENT) {
            mProceedButton.setText(R.string.launch);
            if (canFindTargetPackage()) {
                mProceedButton.setEnabled(true);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
