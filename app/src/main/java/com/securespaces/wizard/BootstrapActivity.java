package com.securespaces.wizard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;
import com.securespaces.wizard.enterprise.common.ViewPagerEx;

import java.util.ArrayList;

public class BootstrapActivity extends AppCompatActivity {
    private static final String TAG = BootstrapActivity.class.getSimpleName();
    public static final String EMM_THUNDERSOFT = "com.thundersoft.mdm";
    public static final String EMM_NATIONSKY = "com.nq.mdm";
    public static final String EMM_NONE = "";
    public static final String KEY_CHOSEN_EMM = "key_chosen_emm";
    public static final String KEY_FINISHED = "key_finished";
    public static final String KEY_PENDING_UNINSTALL = "key_pending_uninstall";
    private static final int NOTIFICATION_ID = 0;

    private static final int MODE_NORMAL = 1;
    private static final int MODE_FASHIONABLE = 2;

    public static final int FRAG_NUMBER = 2;
    public static final int FRAG_FASHION = 0;
    public static final int FRAG_APPS = 1;
    private ArrayList<Integer> mLastPages = new ArrayList<>();

    private int mMode;
    private ArrayList<BootstrapFragment> mFragments;
    private SpacesManager mSpacesManager;
    private SharedPreferences mSharedPrefs;
    private ViewPagerEx mPager;
    private MyPagerAdapter mAdapter;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getData().getEncodedSchemeSpecificPart().equals(getTargetPackage())) {
                targetPackageFound();
            }
        }
    };

    public BootstrapActivity() {
    }

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

        mMode = MODE_FASHIONABLE;
        if (mMode == MODE_NORMAL) {
            mFragments.add(EmmSelectionFragment.newInstance(0));
            mFragments.add(RecommendedAppsFragment.newInstance(1));
        } else if (mMode == MODE_FASHIONABLE) {
            mFragments.add(FashionFragment.newInstance(0));
            mFragments.add(RecommendedAppsFragment.newInstance(1));
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(mBroadcastReceiver, intentFilter);

        mPager = (ViewPagerEx) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        int size = mLastPages.size();
        if (size>0) {
            mPager.setCurrentItem(mLastPages.get(size-1));
            mLastPages.remove(size-1);
        } else
            super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissNotification(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mSharedPrefs.getBoolean(KEY_FINISHED, false)) {
            createNotification(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);

    }

    public static void createNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, BootstrapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.notification_continue_title));
        builder.setContentText(context.getString(R.string.notification_continue_text));
        builder.setSmallIcon(R.drawable.ic_work_white_24dp);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void dismissNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void onProceedPushed(int callingFragmentPosition) {
        if (callingFragmentPosition < mFragments.size() - 1) {
            switchFragmentAnimated(mFragments.get(callingFragmentPosition + 1));
        }
    }

    public void onLaunchPressed() {
        mSharedPrefs.edit().putBoolean(KEY_FINISHED, true).apply();
        if (canFindTargetPackage()) {
            startActivity(getTargetPackageLaunchIntent());
        }
        finish();
    }

    private void switchFragmentAnimated(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }

    public boolean canFindTargetPackage() {
        return getTargetPackageLaunchIntent() != null;
    }

    public Intent getTargetPackageLaunchIntent() {
        return getPackageManager().getLaunchIntentForPackage(getTargetPackage());
    }

    public String getTargetPackage() {
        // this is useful for testing purposes
        //return "com.securespaces.android.xiaomitest.mi";
        return "com.nq.mdm";
    }

    private void targetPackageFound() {
        ((IFinalFragment) mFragments.get(mFragments.size() - 1)).onTargetPackageFound();
    }

    public void onEmmChosen(String emmPackageName) {
        mSharedPrefs.edit().putString(KEY_CHOSEN_EMM, emmPackageName).apply();
    }

    private void disablePackage(String packageName) {
        if (mSpacesManager.isPackageInstalledInSpace(packageName, UserUtils.myUserHandle())) {
            mSpacesManager.setPackageEnabledInSpace(packageName, false, UserUtils.myUserHandle());
        } else {
            mSharedPrefs.edit().putString(KEY_PENDING_UNINSTALL, packageName).apply();
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BootstrapFragment fg;
            switch (position) {

                case FRAG_FASHION:
                    fg = FashionFragment.newInstance(0);

                    return fg;

                case FRAG_APPS:
                    fg = RecommendedAppsFragment.newInstance(1);

                    return fg;

                default:
                    fg = new BootstrapFragment();
                    return fg;
            }
        }

        @Override
        public int getCount() {
            return FRAG_NUMBER;
        }
    }

    public void showFragmentPage(int currentPos, int newPos) {
        if (currentPos >= 0)
            mLastPages.add(currentPos);
        mPager.setCurrentItem(newPos);
    }
}
