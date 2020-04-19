package com.github.tei.imamu.viewmodel.recipe.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe

class RecipeDetailViewModelFactory(private val recipe: Recipe, private val recipeDao: RecipeDao) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java))
        {
            return RecipeDetailViewModel(recipe, recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}