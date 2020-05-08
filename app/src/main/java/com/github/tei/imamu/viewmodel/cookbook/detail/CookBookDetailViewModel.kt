package com.github.tei.imamu.viewmodel.cookbook.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.data.entity.recipe.Recipe
import kotlinx.coroutines.Job

class CookBookDetailViewModel(application: Application, cookBook: CookBook) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private val _cookBook = MutableLiveData<CookBook>()
    val cookBook: LiveData<CookBook>
        get() = _cookBook

    private val _clickedRecipe = MutableLiveData<Recipe>()
    val clickedRecipe: LiveData<Recipe>
        get() = _clickedRecipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    init
    {
        _cookBook.value = cookBook
    }

    fun onClickRecipe(item: Recipe)
    {
        _clickedRecipe.value = item
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToRecipeDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}