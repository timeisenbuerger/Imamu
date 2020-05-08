package com.github.tei.imamu.viewmodel.cookbook.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CookBookListViewModelFactory(private val application: Application) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(CookBookListViewModel::class.java))
        {
            return CookBookListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}