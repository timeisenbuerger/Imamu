package com.github.tei.imamu.viewmodel.recipe.rimport

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImportRecipeViewModelFactory(val application: Application) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ImportRecipeViewModel::class.java))
        {
            return ImportRecipeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}