package com.securespaces.fashion.wizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;

/**
 * Created by eric on 16/08/16.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (!sharedPreferences.getBoolean(BootstrapActivity.KEY_FINISHED, false)) {
                BootstrapActivity.createNotification(context);
            }
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = sharedPreferences.getString(BootstrapActivity.KEY_PENDING_UNINSTALL, "");
            if (packageName.equals(""))
                return;
            if (intent.getData().getEncodedSchemeSpecificPart().equals(packageName)) {
                SpacesManager spacesManager = new SpacesManager(context);
                spacesManager.setPackageEnabledInSpace(packageName, false, UserUtils.myUserHandle());
                sharedPreferences.edit().remove(BootstrapActivity.KEY_PENDING_UNINSTALL).apply();
            }
        }
    }
}
