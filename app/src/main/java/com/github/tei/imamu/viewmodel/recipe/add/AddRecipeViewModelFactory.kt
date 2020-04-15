package com.github.tei.imamu.viewmodel.recipe.add

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.dao.RecipeDao
import java.lang.IllegalArgumentException

class AddRecipeViewModelFactory(private val recipeDao: RecipeDao, private val application: Application) : ViewModelProvider.Factory
{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java))
        {
            return AddRecipeViewModel(recipeDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}