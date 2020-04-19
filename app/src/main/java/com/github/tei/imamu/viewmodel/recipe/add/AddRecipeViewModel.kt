package com.github.tei.imamu.viewmodel.recipe.add

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe
import kotlinx.coroutines.*

class AddRecipeViewModel(private val recipeDao: RecipeDao, application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _recipe = MutableLiveData<Recipe>()
    val recipe : LiveData<Recipe>
        get() = _recipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail : LiveData<Boolean>
        get() = _navigateToRecipeDetail

    init
    {
        _recipe.value = Recipe()
    }

    fun onSaveRecipe()
    {
        uiScope.launch {
            insert(_recipe.value!!)
            _navigateToRecipeDetail.value = true
        }
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }

    private suspend fun insert(recipe: Recipe)
    {
        withContext(Dispatchers.IO) {
            val id = recipeDao.insert(recipe)
            _recipe.value!!.id = id
        }
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}