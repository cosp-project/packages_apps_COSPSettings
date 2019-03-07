/*
 * Copyright (C) 2019 COSP
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

 package com.cosp.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

import com.cosp.settings.R;

 public class ReadingModeSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private SwitchPreference readingModeToggle;
    private static final String READINGMODE_ENABLED_PREFERENCE = "com.cosp.settings.fragments.READINGMODE_ENABLED_PREFERENCE";
    private static final String DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String DISPLAY_DALTONIZER         = "accessibility_display_daltonizer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.cosp_settings_readingmode);

        ContentResolver resolver = getActivity().getContentResolver();
    
        readingModeToggle = (SwitchPreference) findPreference("readingmode_enabled");
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(READINGMODE_ENABLED_PREFERENCE, false);

        readingModeToggle.setChecked(enabled);
        readingModeToggle.setOnPreferenceChangeListener(this);
        
 }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.COSP_SETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == readingModeToggle) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(resolver, Secure.NIGHT_DISPLAY_ACTIVATED, value ? 0 : 1);
            Settings.Secure.putInt(resolver, DISPLAY_DALTONIZER_ENABLED, value ? 1 : 0);
            Settings.Secure.putInt(resolver, DISPLAY_DALTONIZER, value ? 0 : -1);
            readingModeToggle.setChecked(value);
            return true;
        }
        return false;
    }
}