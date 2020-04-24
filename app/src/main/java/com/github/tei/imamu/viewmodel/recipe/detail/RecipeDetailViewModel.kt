package com.github.tei.imamu.viewmodel.recipe.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.Recipe
import kotlinx.coroutines.Job

class RecipeDetailViewModel(recipe: Recipe) : ViewModel()
{
    private var viewModelJob = Job()

    private val _currentRecipe = MutableLiveData<Recipe>()
    val currentRecipe: LiveData<Recipe>
        get() = _currentRecipe

    init
    {
        _currentRecipe.value = recipe
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}