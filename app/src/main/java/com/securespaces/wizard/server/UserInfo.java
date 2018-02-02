package com.securespaces.wizard.server;

import android.content.res.Configuration;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by raymond on 1/6/17.
 */

public class UserInfo {
    public String fullName;
    public String email;
    public String company;
    public String feedback;
    public String timeZone;
    public String locale;
    public ArrayList<ArrayMap> appIds;
    public String additionalNotes;

    public UserInfo(Configuration config) {
        timeZone = "UTC" + TimeZone.getDefault().getRawOffset()/3600000;
        locale = config.locale.getLanguage() + "_" + config.locale.getCountry();
        appIds = new ArrayList<>();
    }
}
