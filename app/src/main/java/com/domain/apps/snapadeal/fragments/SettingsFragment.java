package com.domain.apps.snapadeal.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.domain.apps.snapadeal.R;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static String KEY_PREF_NOTIFY_NEAR = "key_notif_near_store";


    public static boolean isNotifyNearTrue(Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);

        return sharedPref.getBoolean(KEY_PREF_NOTIFY_NEAR, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = "";

        try {
            syncConnPref = sharedPref.getString("pref_key_notification_settings", "");
        } catch (Exception e) {

        }


    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
