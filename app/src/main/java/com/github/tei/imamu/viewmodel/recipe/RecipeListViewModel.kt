package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
{
    private var _recipes: ObjectBoxLiveData<Recipe> = recipeRepository.getAll()
    val recipes: ObjectBoxLiveData<Recipe>
        get() = _recipes

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
        _recipes = recipeRepository.getAll()
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
}