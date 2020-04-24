package com.github.tei.imamu.viewmodel.recipe.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.entity.Recipe

class RecipeDetailViewModelFactory(private val recipe: Recipe) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java))
        {
            return RecipeDetailViewModel(recipe) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}