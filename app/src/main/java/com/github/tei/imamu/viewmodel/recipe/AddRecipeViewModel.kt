package com.github.tei.imamu.viewmodel.recipe

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe
import kotlinx.coroutines.*

class AddRecipeViewModel(val recipeDao: RecipeDao, application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val title = MutableLiveData<String>()
    val servingsNumber = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val preparation = MutableLiveData<String>()

    fun onSaveRecipe()
    {
        uiScope.launch {
            val newRecipe = Recipe()

            initRecipe(newRecipe)
            insert(newRecipe)
        }
    }

    private suspend fun insert(recipe: Recipe)
    {
        withContext(Dispatchers.IO) {
            recipeDao.insert(recipe)
        }
    }

    private fun initRecipe(recipe: Recipe)
    {
        recipe.title = title.value!!
        recipe.servingsNumber = Integer.valueOf(servingsNumber.value!!)
        recipe.ingredients = ingredients.value!!
        recipe.preparation = preparation.value!!
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}