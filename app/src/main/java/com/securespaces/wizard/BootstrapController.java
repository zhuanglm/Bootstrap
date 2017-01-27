package com.securespaces.wizard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.securespaces.wizard.enterprise.common.ViewPagerEx;

import java.security.SignatureSpi;
import java.util.ArrayList;

/**
 * Created by eric on 27/01/17.
 */

public class BootstrapController implements BootstrapContract.UserActionsListener {
    public static final String KEY_FINISHED = "key_finished";

    /**
     * A record of the page numbers the user has visited
     */
    private ArrayList<Integer> mPageHistory = new ArrayList<>();
    private BootstrapActivity mActivity;
    private ArrayList<BootstrapFragment> mFragments;
    private ViewPagerEx mPager;
    private MyPagerAdapter mAdapter;
    private SharedPreferences mSharedPrefs;

    private static BootstrapController sInstance;

    public static BootstrapController getInstance(BootstrapActivity activity) {
        if (sInstance == null) {
            sInstance = new BootstrapController(activity);
        }
        return sInstance;
    }

    public static BootstrapController getInstance() {
        return sInstance;
    }

    private BootstrapController(BootstrapActivity activity) {
        mActivity = activity;

        mFragments = new ArrayList<>();
        mFragments.add(new LoadingFragment());
        mFragments.add(new IntroFragment());
        mFragments.add(new RecommendedAppsFragment());

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        mAdapter = new MyPagerAdapter(mActivity.getSupportFragmentManager());

        mPager = mActivity.getPager();
        mPager.setPagingEnabled(false);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        sInstance = null;
    }

    @Override
    public void onProceed() {
        onProceed(true);
    }

    public void onProceed(boolean history) {
        int currentPage = mPager.getCurrentItem();
        if (history) {
            mPageHistory.add(currentPage);
        }
        mPager.setCurrentItem(++currentPage);
    }

    public void onBack() {
        int size = mPageHistory.size();
        if (size>0) {
            mPager.setCurrentItem(mPageHistory.get(size-1));
            mPageHistory.remove(size-1);
        } else {
            mActivity.finish();
        }
    }

    public void onLaunchPressed() {
        mSharedPrefs.edit().putBoolean(KEY_FINISHED, true).apply();
        if (canFindTargetPackage()) {
            mActivity.startActivity(getTargetPackageLaunchIntent());
        }
        mActivity.finish();
    }

    /**
     * Advance on a page load, if the current page is the first one.
     */
    public void onPageLoaded() {
        if (mPager.getCurrentItem() == 0) {
            onProceed(false);
        }
    }

    public boolean canFindTargetPackage() {
        return getTargetPackageLaunchIntent() != null;
    }

    public Intent getTargetPackageLaunchIntent() {
        return mActivity.getPackageManager().getLaunchIntentForPackage(getTargetPackage());
    }

    public String getTargetPackage() {
        // this is useful for testing purposes
        //return "com.securespaces.android.xiaomitest.mi";
        return "com.nq.mdm";
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
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
    }
}
