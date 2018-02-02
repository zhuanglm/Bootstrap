package com.securespaces.wizard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.securespaces.android.ssm.SpaceInfo;
import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;

import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_INSTALL_UNKNOWN_SOURCES;
import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_OUTGOING_CALLS;
import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_SHARE_LOCATION;
import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_SMS;
import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_UNMUTE_MICROPHONE;
import static com.securespaces.android.ssm.SpaceRestrictions.DISALLOW_USB_FILE_TRANSFER;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISABLE_SCREEN_LOCK;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_BLUETOOTH;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_CAMERA_ACCESS;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_NFC;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_READ_DEVICE_INFO;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_SCREENSHOT;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_SPACE_DEBUG;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_SPACE_NOTIFICATIONS;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_SPACE_SHARING_RECEIVE;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_DISALLOW_SPACE_SHARING_SEND;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_ENSURE_ALLOW_DELETE_USER_DATA;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_ENSURE_EXCLUSIVE_NETWORK_ACCESS;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_ENSURE_LOCK_OTHER_SPACES_ON_ENTER;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_ENSURE_SPACE_AUTO_CLEAN;
import static com.securespaces.android.ssm.SpaceRestrictions.SS_ENSURE_STOP_SPACE_ON_EXIT;


/**
 * Created by raymond on 12/6/16.
 */

public class SetRestrictionsTask extends AsyncTask<Integer, String, Void> {
    private static final String TAG = SetRestrictionsTask.class.getSimpleName();

    private Context mContext;
    private FragmentBootstrapSetSecurity mFragment;
    private ProgressDialog mProgressDialog;
    //private MiUIDialog mDialog;

    private SpacesManager mSpacesManager;
    private SpaceInfo mSpaceInfo;
    private ArrayMap<String,String> mRestrictionKeys;

    public static void setRestrictionsTask(FragmentBootstrapSetSecurity fragment, int arrayID) {
        if (fragment == null) {
            Log.e(TAG, "createSpace - context is null, abort");
            return;
        }
        SetRestrictionsTask task = new SetRestrictionsTask(fragment.getActivity(),fragment);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,arrayID);
    }

    public SetRestrictionsTask(Context context, FragmentBootstrapSetSecurity fragment) {
        mContext = context;
        mFragment = fragment;
        mProgressDialog = new ProgressDialog(mContext);
        //mDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        //mProgressDialog.setCancelable(false);
        //mProgressDialog.show();

        mSpacesManager = new SpacesManager(mContext);
        mSpaceInfo = mSpacesManager.getSpace(UserUtils.myUserHandle());

        initRestrictionKeys();
    }


    @Override
    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(Void p) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mContext != null)
            Toast.makeText(mContext, mContext.getResources().getText(R.string.update_security),
                Toast.LENGTH_SHORT).show();
        mFragment.updateButton(true);
//        mDialog.dismiss();
//        mFragment.onProceed(true);

    }

    protected void setSpaceRestriction(String restrictionKey, boolean isRestricted) {
        if (isRestricted) {
            mSpacesManager.addSpaceRestriction(restrictionKey, UserUtils.myUserHandle());
        } else {
            mSpacesManager.clearSpaceRestriction(restrictionKey, UserUtils.myUserHandle());
        }
    }

    @Override
    protected Void doInBackground(Integer... arrayID) {

        ArrayMap<String, String> items = parseStringArray(arrayID[0]);

        for (ArrayMap.Entry<String, String> entry : items.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            String restriction = mRestrictionKeys.get(k);
            if (restriction != null && restriction.length() != 0) {
                if (restriction.equals(SS_DISALLOW_CAMERA_ACCESS)) {
                    if (v.equals("ON") || v.equals("Enabled")) {
                        mSpacesManager.clearSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
                        mSpacesManager.setCameraMode(mSpaceInfo.getUserHandle(),
                                SpacesManager.CAMERA_MODE_ALLOW_IN_SPACE);
                    } else {
//                        mSpacesManager.addSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
//                        mSpacesManager.setCameraMode(mSpaceInfo.getUserHandle(),
//                                SpacesManager.CAMERA_MODE_DISALLOW_IN_SPACE);
                    }
                } else if (restriction.equals(SS_DISALLOW_SPACE_NOTIFICATIONS)) {
                    int stat ;
                    if (v.equals("All")) {
                        stat = SpacesManager.CROSS_SPACE_NOTIFICATION_STATE_FULL;
                    } else if (v.equals("Hide Sensitive")) {
                        stat = SpacesManager.CROSS_SPACE_NOTIFICATION_STATE_HIDE_SENSITIVE;
                    } else if (v.equals("Badge Only")) {
                        stat = SpacesManager.CROSS_SPACE_NOTIFICATION_STATE_BADGE_ONLY;
                    } else if (v.equals("No")) {
                        stat = SpacesManager.CROSS_SPACE_NOTIFICATION_STATE_DISABLED;
                    } else {
                        Log.e(TAG, "Could not parse new cross-space notification mode: " + v);
                        continue;
                    }

                    try {
                        mSpacesManager.setCrossSpaceNotificationState(mSpaceInfo.getUserHandle(),stat);
                    } catch (NumberFormatException nfe) {
                        Log.e(TAG, "Could not parse new cross-space notification mode", nfe);
                    }
                } else if (restriction.equals("pref_exit_on_sleep")) {
                    if (!v.equals("Default")) {
                        int time = 0;
                        if (v.indexOf("seconds") > 0) {
                            String[] s = v.split(" ");
                            time = Integer.parseInt(s[0]);
                        } else if (v.indexOf("minutes") > 0) {
                            String[] s = v.split(" ");
                            time = Integer.parseInt(s[0]) * 60;
                        } else {
                            Log.e(TAG, "Could not parse exit on sleep setup");
                        }

                        if (time >= 0)
                            mSpacesManager.setSpaceExitOnSleepDelay(mSpaceInfo.getUserHandle(), time * 1000);

                    }
                } else if (restriction.equals(SS_ENSURE_STOP_SPACE_ON_EXIT) ||
                        restriction.equals(SS_ENSURE_EXCLUSIVE_NETWORK_ACCESS) ||
                        restriction.equals(SS_ENSURE_LOCK_OTHER_SPACES_ON_ENTER) ||
                        restriction.equals(SS_ENSURE_SPACE_AUTO_CLEAN)) {
                    if (v.equals("ON") || v.equals("Enabled")) {
                        mSpacesManager.addSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
                    } else {
                        mSpacesManager.clearSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
                    }

                } else {
                    if (v.equals("ON") || v.equals("Enabled")) {
                        mSpacesManager.clearSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
                    } else {
                        mSpacesManager.addSpaceRestriction(restriction, mSpaceInfo.getUserHandle());
                    }
                }
            }
            publishProgress(k,"...",v);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public ArrayMap<String,String> parseStringArray(int stringArrayResourceId) {
        String[] stringArray = mContext.getResources().getStringArray(stringArrayResourceId);
        ArrayMap<String,String> outputArray = new ArrayMap<>(stringArray.length);
        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            outputArray.put(splitResult[0], splitResult[1]);
        }
        return outputArray;
    }

    private void initRestrictionKeys() {
        mRestrictionKeys = new ArrayMap<>();

        mRestrictionKeys.put("Location Services",DISALLOW_SHARE_LOCATION);
        mRestrictionKeys.put("NFC",SS_DISALLOW_NFC);
        mRestrictionKeys.put("SMS",DISALLOW_SMS);
        mRestrictionKeys.put("ScreenShot",SS_DISALLOW_SCREENSHOT);
        mRestrictionKeys.put("Sharing out of Space",SS_DISALLOW_SPACE_SHARING_SEND);
        mRestrictionKeys.put("Stop Space on Exit", SS_ENSURE_STOP_SPACE_ON_EXIT);
        mRestrictionKeys.put("Exclusive Network Access",SS_ENSURE_EXCLUSIVE_NETWORK_ACCESS);
        mRestrictionKeys.put("Allow User Data Deletion",SS_ENSURE_ALLOW_DELETE_USER_DATA);
        mRestrictionKeys.put("Bluetooth",SS_DISALLOW_BLUETOOTH);
        mRestrictionKeys.put("Outgoing Calls",DISALLOW_OUTGOING_CALLS);
        mRestrictionKeys.put("Microphone",DISALLOW_UNMUTE_MICROPHONE);
        mRestrictionKeys.put("Read Device Info",SS_DISALLOW_READ_DEVICE_INFO);
        mRestrictionKeys.put("Sharing into Space",SS_DISALLOW_SPACE_SHARING_RECEIVE);
        mRestrictionKeys.put("Lock other Spaces on Entry",SS_ENSURE_LOCK_OTHER_SPACES_ON_ENTER);
        mRestrictionKeys.put("Delete user data on exit",SS_ENSURE_SPACE_AUTO_CLEAN);
        mRestrictionKeys.put("Allow USB connection",DISALLOW_USB_FILE_TRANSFER);
        mRestrictionKeys.put("Notifications",SS_DISALLOW_SPACE_NOTIFICATIONS);
        mRestrictionKeys.put("Camera",SS_DISALLOW_CAMERA_ACCESS);
        mRestrictionKeys.put("Debugging",SS_DISALLOW_SPACE_DEBUG);
        mRestrictionKeys.put("Screen Lock allowed",SS_DISABLE_SCREEN_LOCK);
        mRestrictionKeys.put("Authentication",DISALLOW_INSTALL_UNKNOWN_SOURCES);
        mRestrictionKeys.put("Auth Type","");
        mRestrictionKeys.put("Password Minimum Length","");
        mRestrictionKeys.put("Password Expiration","");
        mRestrictionKeys.put("Password recovery","pref_password_recovery");
        mRestrictionKeys.put("Exit on Sleep","pref_exit_on_sleep");
    }
}
