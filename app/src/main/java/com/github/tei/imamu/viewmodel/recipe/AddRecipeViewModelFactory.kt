package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AddRecipeViewModelFactory : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java))
        {
            return AddRecipeViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}