package com.securespaces.android.bootstrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by eric on 16/08/16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (!sharedPreferences.getBoolean(BootstrapActivity.KEY_FINISHED, false)) {
                BootstrapActivity.createNotification(context);
            }
        }
    }
}
