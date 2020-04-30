package com.github.tei.imamu.viewmodel.recipe.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class AddRecipeViewModel(application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe>
        get() = _recipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    private val recipeBox: Box<Recipe> = ObjectBox.boxStore.boxFor()

    init
    {
        _recipe.value = Recipe()
    }

    fun onSaveRecipe()
    {
        recipe.value?.let { recipeBox.put(it) }
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}