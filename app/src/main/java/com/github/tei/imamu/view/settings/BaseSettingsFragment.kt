package com.github.tei.imamu.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.tei.imamu.R

class BaseSettingsFragment : PreferenceFragmentCompat()
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        addPreferencesFromResource(R.xml.root_preferences)

//        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        //
        //        val downloadImages: Preference? = findPreference<CheckBoxPreference>("pref_download_recipe_images")
        //        downloadImages?.setOnPreferenceChangeListener { preference, newValue -> defaultSharedPreferences.}
    }
}