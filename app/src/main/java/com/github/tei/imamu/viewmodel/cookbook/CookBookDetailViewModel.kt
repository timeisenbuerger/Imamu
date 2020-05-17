package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.CookBookRepository

class CookBookDetailViewModel(private val cookBookRepository: CookBookRepository) : ViewModel()
{
    val cookBook = MutableLiveData<CookBook>()

    private val _clickedRecipe = MutableLiveData<Recipe>()
    val clickedRecipe: LiveData<Recipe>
        get() = _clickedRecipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    fun onClickRecipe(item: Recipe)
    {
        _clickedRecipe.value = item
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToRecipeDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }
}