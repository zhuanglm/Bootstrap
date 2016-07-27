package com.securespaces.android.bootstrap;


import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.securespaces.android.ssm.SpacesManager;

import java.util.Locale;

public class SsrmQueryParams {
    private String platformVersion;
    private String maker;
    private String model;
    private String language;
    private String ssVersion;
    private String mParamString;
    private String mDeviceID;
    private Context mContext;

    public SsrmQueryParams(Context context) {
        mContext = context;
        platformVersion = Build.VERSION.RELEASE;
        maker = Build.MANUFACTURER;
        model = Build.MODEL;
        language = Locale.getDefault().toString();
        ssVersion = SpacesManager.getSsVersion();
        mParamString = "";
        mDeviceID = getDeviceID();
    }

    public String toString() {
        if (mParamString.isEmpty()) {
            appendParamString("platformVersion", platformVersion);
            appendParamString("maker", maker);
            appendParamString("model", model);
            appendParamString("language", language);
            appendParamString("ssVersion", ssVersion);
            appendParamString("deviceID", mDeviceID);
        }
        return mParamString;
    }

    private String getDeviceID()
    {
        String deviceId = "unknown";
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null)
        {
            deviceId = tm.getDeviceId();
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Build.SERIAL;
            }
        }
        return  deviceId;
    }

    private void appendParamString(String paramToken, String paramValue) {
        if ((paramValue == null) || paramValue.isEmpty()) {
            return;
        }

        if (mParamString.isEmpty()) {
            // This is really ugly. FIXME
            mParamString += "&";
        } else {
            mParamString += "&";
        }
        mParamString += paramToken + "=" + paramValue;
    }
}