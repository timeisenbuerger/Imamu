package com.github.tei.imamu.viewmodel.recipe

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

    init
    {
        _recipe.value = Recipe()
    }

    fun onSaveRecipe()
    {
        uiScope.launch {
            insert(_recipe.value!!)
        }
    }

    private suspend fun insert(recipe: Recipe)
    {
        withContext(Dispatchers.IO) {
            recipeDao.insert(recipe)
        }
    }


    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}