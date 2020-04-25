package com.github.tei.imamu.viewmodel.recipe.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.entity.Recipe

class EditRecipeViewModelFactory(private val recipe: Recipe, private val application: Application) : ViewModelProvider.Factory
{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(EditRecipeViewModel::class.java))
        {
            return EditRecipeViewModel(recipe, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}