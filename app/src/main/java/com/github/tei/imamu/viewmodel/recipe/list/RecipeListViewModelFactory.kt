package com.github.tei.imamu.viewmodel.recipe.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class RecipeListViewModelFactory(private val application: Application) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java))
        {
            return RecipeListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}