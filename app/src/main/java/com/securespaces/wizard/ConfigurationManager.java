/*******************************************************************************
 * GRAPHITE SOFTWARE CONFIDENTIAL
 *
 * Copyright 2013-2017. Graphite Software Corporation. All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Graphite Software Corporation and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to Graphite Software
 * Corporation and its suppliers and may be covered by Canada, U.S. and Foreign
 * Patents, patents in process, and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from Graphite
 * Software Corporation.
 ******************************************************************************/
package com.securespaces.wizard;

import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;

import java.util.Locale;

/**
 * A convenience class for reading in app restriction data.
 */

public class ConfigurationManager {
    // page 1 restrictions
    public static String FRONT_PAGE_BACKGROUND_URL = "front_page_background_url";
    public static String TEXT_COLOR_PRIMARY = "text_color_primary";
    public static String TITLE_LINE_1 = "title_line_1";
    public static String TITLE_LINE_2 = "title_line_2";
    public static String TAG_LINE = "tag_line";
    public static String BULLET_1 = "bullet_1";
    public static String BULLET_2 = "bullet_2";
    public static String BULLET_3 = "bullet_3";
    public static String BUTTON_COLOR = "button_color";
    public static String BUTTON_TEXT_COLOR = "button_text_color";
    public static String BUTTON_TEXT = "button_text";
    public static String BUTTON_ACTION = "button_action";

    // page 2 restrictions
    public static String HEADER_COLOR = "header_color";
    public static String HEADER_TEXT_COLOR = "header_text_color";
    public static String HEADER_TEXT = "header_text";
    public static String PAGE_2_BACKGROUND_URL = "page_2_background_url";
    public static String BUTTON_COLOR_2 = "button_color_2";
    public static String BUTTON_TEXT_COLOR_2 = "button_text_color_2";
    public static String BUTTON_TEXT_2 = "button_text_2";
    public static String BUTTON_ACTION_2 = "button_action_2";

    // button actions
    /**
     * Clicking the button will move to the next page
     */
    public static String ACTION_BUTTON_NEXT_PAGE = "action_button_next_page";
    /**
     * Clicking the button will close the bootstrap.
     */
    public static String ACTION_BUTTON_CLOSE = "action_button_close";
    /**
     * Clicking the button will close the bootstrap and launch the target app, if it can be found.
     */
    public static String ACTION_BUTTON_LAUNCH = "action_button_launch";

    /**
     * Language parameter in a localized key string
     */
    public static final String PARAM_LANGUAGE = "language";
    /**
     * Key paramater in a localized key string.
     */
    public static final String PARAM_KEY = "key";

    /**
     * Return the app restriction string associated with the given key, or null if no such restriction
     * has been set.
     *
     * Attempts to find a localized version of the app restriction string using Locale.DEFAULT.
     * Localization scheme: "language=en-US&key=this is a key"
     *
     * @param context Context to access UserManager.
     * @param key Key for the app restriction data bundle.
     * @return Value associated with key, or null if none is found.
     */
    public static String getData(Context context, String key) {
        UserManager userManager = (UserManager)context.getSystemService(Context.USER_SERVICE);
        Bundle bundle = userManager.getApplicationRestrictions(context.getPackageName());

        // check for a localized version of this key
        Locale currentLocale = Locale.getDefault();
        String localizedKey = PARAM_LANGUAGE + "=" + currentLocale.toLanguageTag() +"&"  + PARAM_KEY + "=" + key;
        String result = bundle.getString(localizedKey);
        // if no localized string can be found, just use the default
        if (result == null) {
            result = bundle.getString(key);
        }
        return result;
    }
}
