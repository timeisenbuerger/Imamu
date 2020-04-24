package com.github.tei.imamu.viewmodel.recipe.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.Recipe
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class RecipeListViewModel(application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    var recipes = MutableLiveData<List<Recipe>>()

    private val _navigateToDetail = MutableLiveData<Recipe>()
    val navigateToDetail: LiveData<Recipe>
        get() = _navigateToDetail

    private val recipeBox: Box<Recipe> = ObjectBox.boxStore.boxFor()

    init
    {
        initRecipes()
    }

    fun initRecipes()
    {
        recipes.value = recipeBox.all
    }

    fun deleteRecipes(recipes: List<Recipe>)
    {
        recipeBox.remove(recipes)
    }

    fun onRecipeClicked(recipe: Recipe)
    {
        _navigateToDetail.value = recipe
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