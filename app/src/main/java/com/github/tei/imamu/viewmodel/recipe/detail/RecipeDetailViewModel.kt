package com.github.tei.imamu.viewmodel.recipe.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe
import kotlinx.coroutines.*

class RecipeDetailViewModel(private val recipeId: Long = 0L, private val recipeDao: RecipeDao) : ViewModel()
{
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _currentRecipe = MutableLiveData<Recipe>()
    val currentRecipe: LiveData<Recipe>
        get() = _currentRecipe

    init
    {
        initRecipe()
    }

    private fun initRecipe()
    {
        uiScope.launch {
            _currentRecipe.value = getRecipeById()
        }
    }

    private suspend fun getRecipeById(): Recipe?
    {
        return withContext(Dispatchers.IO)
        {
            var recipe = recipeDao.getSingle(recipeId)
            recipe
        }
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}