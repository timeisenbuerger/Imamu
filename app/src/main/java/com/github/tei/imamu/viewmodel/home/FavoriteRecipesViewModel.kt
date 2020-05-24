package com.github.tei.imamu.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.android.ObjectBoxLiveData

class FavoriteRecipesViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
{
    private var _recipes: ObjectBoxLiveData<Recipe> = recipeRepository.getAllFavorites()
    val recipes: ObjectBoxLiveData<Recipe>
        get() = _recipes

    private var _navigateToDetail = MutableLiveData<Recipe>()
    val navigateToDetail: LiveData<Recipe>
        get() = _navigateToDetail

    fun onRecipeClicked(recipe: Recipe)
    {
        _navigateToDetail.value = recipe
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }
}