package com.github.tei.imamu.viewmodel.recipe.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeDetailViewModelFactory() : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java))
        {
            return RecipeDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}