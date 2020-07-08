package com.github.tei.imamu.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.tei.imamu.R

class BaseSettingsFragment : PreferenceFragmentCompat()
{
    private var listPreference: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.base_preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        listPreference = preferenceManager.findPreference("preference_key") as ListPreference?
        listPreference?.let {
            it.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue -> true }
        }

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}