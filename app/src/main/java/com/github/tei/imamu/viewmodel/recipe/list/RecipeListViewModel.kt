package com.github.tei.imamu.viewmodel.recipe.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe
import kotlinx.coroutines.*

class RecipeListViewModel(private val recipeDao: RecipeDao, application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val recipes = recipeDao.getAll()

    private val _navigateToDetail = MutableLiveData<Long>()
    val navigateToDetail: LiveData<Long>
        get() = _navigateToDetail

    init
    {
    }

//    private fun initRecipes()
//    {
//        uiScope.launch {
//            getAllRecipes()
//        }
//    }
//
//    private suspend fun getAllRecipes()
//    {
//        withContext(Dispatchers.IO) {
//            recipes = recipeDao.getAll()
//        }
//    }

    fun onRecipeClicked(id: Long)
    {
        _navigateToDetail.value = id
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}