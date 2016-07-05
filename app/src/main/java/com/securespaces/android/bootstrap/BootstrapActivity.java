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

public class BootstrapActivity extends AppCompatActivity {
    private static final String TAG = "Bootstrap";
    private static final int FINAL_FRAGMENT = 2;

    // com.securespaces.android.xiaomitest.mi
    // com.nq.mdm

    private ArrayList<Fragment> mFragments;
    private Button mProceedButton;
    private int mCurrentPosition;

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

        mFragments = new ArrayList<>();
        mFragments.add(new FragmentOne());
        mFragments.add(new FragmentTwo());
        mFragments.add(new FragmentThree());

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);

        gotoFragment(mFragments.get(0));
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

    public void switchFragment(int callingFragmentPosition) {
        switch (callingFragmentPosition) {
            case 0:
                switchFragment(mFragments.get(1));
                break;
            case 1:
                switchFragment(mFragments.get(2));
                break;
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
    public static boolean canFindTargetPackage(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(getTargetPackage(context)) != null;
    }

    private static String getTargetPackage(Context context) {
        //return "com.securespaces.android.xiaomitest.mi";
        return context.getString(R.string.target_package);
    }

    private void targetPackageFound() {
        ((FragmentThree)mFragments.get(2)).onTargetPackageFound();
    }
}
