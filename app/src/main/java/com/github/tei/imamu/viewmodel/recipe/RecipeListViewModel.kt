package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.android.ObjectBoxLiveData

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
{
    private var _recipes: ObjectBoxLiveData<Recipe> = recipeRepository.getAll()
    val recipes: ObjectBoxLiveData<Recipe>
        get() = _recipes

    private val _navigateToDetail = MutableLiveData<Recipe>()
    val navigateToDetail: LiveData<Recipe>
        get() = _navigateToDetail

    private val _startRecipeIntent = MutableLiveData<Recipe>()
    val startRecipeIntent: LiveData<Recipe>
        get() = _startRecipeIntent

    fun initRecipes()
    {
        _recipes = recipeRepository.getAll()
    }

    fun deleteRecipes(recipes: List<Recipe>)
    {
        for (recipe in recipes)
        {
            recipeRepository.remove(recipe)
        }
    }

    fun onRecipeClicked(recipe: Recipe)
    {
        _navigateToDetail.value = recipe
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }

    fun onShareRecipe(item: Recipe)
    {
        _startRecipeIntent.value = item
    }

    fun onShareRecipeComplete()
    {
        _startRecipeIntent.value = null
    }
}