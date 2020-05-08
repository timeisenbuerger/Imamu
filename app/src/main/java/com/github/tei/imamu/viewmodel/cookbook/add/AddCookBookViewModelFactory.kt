package com.github.tei.imamu.viewmodel.cookbook.add

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddCookBookViewModelFactory(private val application: Application) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(AddCookBookViewModel::class.java))
        {
            return AddCookBookViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}