/*
 * Copyright (C) 2016 The Pure Nexus Project
 * used for COSP OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosp.settings;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import com.android.settings.R;
import com.cosp.settings.preferences.Utils;

import com.android.settings.SettingsPreferenceFragment;

public class COSPSettings extends SettingsPreferenceFragment {

    public static final int KEY_MASK_BACK = 0x02;

    private static final String ACTIVE_EDGE_CATEGORY = "active_edge_category";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final String KEY_DEVICE_PART = "device_part";
        final String KEY_DEVICE_PART_PACKAGE_NAME = "org.omnirom.device";

        addPreferencesFromResource(R.xml.cosp_settings);
		
		PackageManager pm = getPackageManager();

        try {
            pm.getPackageInfo("com.updates", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            getPreferenceManager().findPreference("updates").setVisible(false);
        }

        Preference ActiveEdge = findPreference(ACTIVE_EDGE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_active_edge)) {
            getPreferenceScreen().removePreference(ActiveEdge);
        } else {
            if (!getContext().getPackageManager().hasSystemFeature(
                    "android.hardware.sensor.assist")) {
                getPreferenceScreen().removePreference(ActiveEdge);
            }
        }
		
        // DeviceParts
        if (!Utils.isPackageInstalled(getActivity(), KEY_DEVICE_PART_PACKAGE_NAME)) {
            getPreferenceScreen().removePreference(findPreference(KEY_DEVICE_PART));
	}

        //TODO: better hw key detect mechanism
        final int deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);
        final boolean hasBackKey = (deviceKeys & KEY_MASK_BACK) != 0;
        if (!hasBackKey) {
            getPreferenceManager().findPreference("buttonsettings_category").setVisible(false);
        }

    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.COSP_SETTINGS;
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }
}