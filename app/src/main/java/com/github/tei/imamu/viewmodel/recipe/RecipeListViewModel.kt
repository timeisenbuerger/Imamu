package com.github.tei.imamu.viewmodel.recipe

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.LastViewedRecipe
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.LastViewedRecipeRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import com.github.tei.imamu.util.ImageUtil
import io.objectbox.android.ObjectBoxLiveData
import java.io.File

class RecipeListViewModel(private val recipeRepository: RecipeRepository, private val lastViewedRecipeRepository: LastViewedRecipeRepository) : ViewModel()
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

    fun deleteRecipes(recipes: List<Recipe>, context: Context)
    {
        val lastViewedRecipes = lastViewedRecipeRepository.getAllAsLazyList()

        for (recipe in recipes)
        {
            var lastViewedRecipeToRemove: LastViewedRecipe? = null
            for (lastViewedRecipe in lastViewedRecipes)
            {
                if (lastViewedRecipe.recipe.target == recipe)
                {
                    lastViewedRecipeToRemove = lastViewedRecipe
                    break
                }
            }

            lastViewedRecipeToRemove?.let {
                lastViewedRecipeRepository.remove(it)
            }

            if (!TextUtils.isEmpty(recipe.imagePath) && recipe.imagePath.contains("RezeptFotos"))
            {
                val image = File(recipe.imagePath)
                ImageUtil.deleteImage(context, image)
            }

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