package com.securespaces.android.bootstrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;

import java.security.Key;
import java.util.ArrayList;

public class BootstrapActivity extends AppCompatActivity {
    private static final String TAG = BootstrapActivity.class.getSimpleName();
    public static final String EMM_THUNDERSOFT = "com.thundersoft.mdm";
    public static final String EMM_NATIONSKY = "com.nq.mdm";

    private static final String KEY_CHOSEN_EMM = "key_chosen_emm";

    private ArrayList<Fragment> mFragments;
    private SpacesManager mSpacesManager;
    private SharedPreferences mSharedPrefs;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getData().getEncodedSchemeSpecificPart().equals(getTargetPackage(getApplicationContext()))) {
                targetPackageFound();
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

        mSpacesManager = new SpacesManager(this);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mFragments = new ArrayList<>();
        mFragments.add(FragmentOne.newInstance(0));
        mFragments.add(EmmSelectionFragment.newInstance(1));
        mFragments.add(RecommendedAppsFragment.newInstance(2));

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);

        switchFragment(mFragments.get(0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void onProceedPushed(int callingFragmentPosition) {
        if (callingFragmentPosition < mFragments.size() - 1) {
            switchFragmentAnimated(mFragments.get(callingFragmentPosition+1));
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void switchFragmentAnimated(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }
    public boolean canFindTargetPackage(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(getTargetPackage(context)) != null;
    }

    private String getTargetPackage(Context context) {
        // this is useful for testing purposes
        //return "com.securespaces.android.xiaomitest.mi";
        //return context.getString(R.string.target_package);
        return mSharedPrefs.getString(KEY_CHOSEN_EMM,"");
    }

    private void targetPackageFound() {
        ((IFinalFragment)mFragments.get(mFragments.size()-1)).onTargetPackageFound();
    }

    public void onEmmChosen(String emmPackageName) {
        mSharedPrefs.edit().putString(KEY_CHOSEN_EMM, emmPackageName).apply();
        if (emmPackageName.equals(EMM_NATIONSKY)) {
            mSpacesManager.setPackageEnabledInSpace(EMM_THUNDERSOFT, false, UserUtils.myUserHandle());
        } else if (emmPackageName.equals(EMM_THUNDERSOFT)){
            mSpacesManager.setPackageEnabledInSpace(EMM_NATIONSKY, false, UserUtils.myUserHandle());
        }

    }
}
