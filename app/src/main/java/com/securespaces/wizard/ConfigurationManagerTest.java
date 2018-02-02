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

import android.os.Bundle;

public class ConfigurationManagerTest extends ConfigurationManager{

    public static Bundle sBundleTestData = new Bundle();

    static {
        sBundleTestData.putString(TEXT_COLOR_PRIMARY, "#FFFFFF");
        sBundleTestData.putString(KEY_FRAGMENTS_CONFIG, "fragment_intro,fragment_contact,fragment_password,fragment_security,fragment_recommended_apps,"+FragmentBootstrapEnd.ID);
        sBundleTestData.putString(HEADER_COLOR, "#2222FF");
        sBundleTestData.putString(HEADER_TEXT_COLOR, "#FFFFFF");

        sBundleTestData.putString(TAG_LINE, "Tag line");
        sBundleTestData.putString(TITLE_LINE_1, "Title 1");
        sBundleTestData.putString(TITLE_LINE_2, "Title 2");

        sBundleTestData.putString(BULLET_1, "B1");
        sBundleTestData.putString(BULLET_2, "B2");
        sBundleTestData.putString(BULLET_3, "B3");

//        sBundleTestData.putString(BUTTON_ACTION_2, ACTION_BUTTON_CLOSE);
//        sBundleTestData.putString(BUTTON_TEXT_2, "Close");
    }
}
