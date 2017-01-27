package com.securespaces.wizard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.securespaces.wizard.enterprise.common.ViewPagerEx;

public class BootstrapActivity extends AppCompatActivity {
    private static final String TAG = BootstrapActivity.class.getSimpleName();
    private static final int NOTIFICATION_ID = 0;

    private ViewPagerEx mPager;
    private BootstrapContract.UserActionsListener mListener;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_bootstrap);
        mPager = (ViewPagerEx)findViewById(R.id.pager);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mListener = BootstrapController.getInstance(this);
    }

    public ViewPagerEx getPager() {
        return mPager;
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissNotification(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mSharedPrefs.getBoolean(BootstrapController.KEY_FINISHED, false)) {
            createNotification(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mListener.onBack();
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
}
