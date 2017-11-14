package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    // Do steps 5 - 11 within SettingsFragment
    // DONE (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

    // DONE (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    // TODO: I use Preference and *String*

    // DONE (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

    // Do step 9 within onCreatePreference
    // DONE (9) Set the preference summary on each preference that isn't a CheckBoxPreference

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        final SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        final int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            final Preference preference = preferenceScreen.getPreference(i);
            /* Note: we exclude CheckBoxPreferences because we cannot call getString() for them
             * and they already have their defaults specified in the xml file using summaryOff and summaryOn
             */
            if (!(preference instanceof CheckBoxPreference)) {
                final String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    // DONE (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // DONE (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    @Override
    public void onStop() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    // DONE (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                final String value = sharedPreferences.getString(key, "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    /**
     * Will set the summary for the provided preference.
     * @param preference the preference for which we set the summary
     * @param value the current value in the shared preferences: e.g. "metric", "imperial"
     */
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            final int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Now get the display label for the selected item in the list-preference
                // e.g. "Metric", "Imperial"
                final CharSequence prefValue = listPreference.getEntries()[prefIndex];
                listPreference.setSummary(prefValue);
            }
        } else if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary(editTextPreference.getText());
        }
    }

}
