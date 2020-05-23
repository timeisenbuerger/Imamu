package com.github.tei.imamu.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.cookbook.LastViewedCookBook
import com.github.tei.imamu.data.database.entity.recipe.LastViewedRecipe
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.CookBookRepository
import com.github.tei.imamu.data.repository.LastViewedCookBookRepository
import com.github.tei.imamu.data.repository.LastViewedRecipeRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.android.ObjectBoxLiveData

class HomeViewModel(private val recipeRepository: RecipeRepository, private val lastViewedRecipeRepository: LastViewedRecipeRepository, private val lastViewedCookBookRepository: LastViewedCookBookRepository) : ViewModel()
{
    private var _favoriteRecipes: ObjectBoxLiveData<Recipe> = recipeRepository.getAllFavorites()
    val favoriteRecipes: ObjectBoxLiveData<Recipe>
        get() = _favoriteRecipes

    private var _lastViewedRecipes: ObjectBoxLiveData<LastViewedRecipe> = lastViewedRecipeRepository.getAll()
    val lastViewedRecipes: ObjectBoxLiveData<LastViewedRecipe>
        get() = _lastViewedRecipes

    private var _lastViewedCookBooks: ObjectBoxLiveData<LastViewedCookBook> = lastViewedCookBookRepository.getAll()
    val lastViewedCookBooks: ObjectBoxLiveData<LastViewedCookBook>
        get() = _lastViewedCookBooks

    private val _navigateToRecipeDetail = MutableLiveData<Recipe>()
    val navigateToRecipeDetail: LiveData<Recipe>
        get() = _navigateToRecipeDetail

    private val _navigateToCookBookDetail = MutableLiveData<CookBook>()
    val navigateToCookBookDetail: LiveData<CookBook>
        get() = _navigateToCookBookDetail

    fun onRecipeClicked(recipe: Recipe)
    {
        _navigateToRecipeDetail.value = recipe
    }

    fun onCookBookClicked(cookBook: CookBook)
    {
        _navigateToCookBookDetail.value = cookBook
    }

    fun onNavigateToRecipeDetailComplete()
    {
        _navigateToRecipeDetail.value = null
    }

    fun onNavigateToCookBookDetailComplete()
    {
        _navigateToCookBookDetail.value = null
    }
}