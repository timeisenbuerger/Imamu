package com.github.tei.imamu.view.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.tei.imamu.R
import kotlin.concurrent.fixedRateTimer

class SettingsFragment : PreferenceFragmentCompat()
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        addPreferencesFromResource(R.xml.root_preferences)
    }
}