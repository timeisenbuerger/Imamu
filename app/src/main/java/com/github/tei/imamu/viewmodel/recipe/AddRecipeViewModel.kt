package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository

class AddRecipeViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe>
        get() = _recipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    init
    {
        _recipe.value = Recipe()
    }

    fun onSaveRecipe()
    {
        _recipe.value?.let {
            for (recipeIngredient in it.recipeIngredients)
            {
                if (recipeIngredient.ingredient.target.id == 0L)
                {
                    recipeIngredient.ingredient.target = ingredientRepository.getIngredientForName(recipeIngredient.ingredient.target.name)
                }
            }

            var totalTime = 0
            if (it.preparationTime.isNotEmpty())
            {
                totalTime += it.preparationTime.toInt()
            }
            if (it.bakingTime.isNotEmpty())
            {
                totalTime += it.bakingTime.toInt()
            }
            if (it.restTime.isNotEmpty())
            {
                totalTime += it.restTime.toInt()
            }
            it.totalTime = totalTime

            recipeRepository.save(it)
        }
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }
}