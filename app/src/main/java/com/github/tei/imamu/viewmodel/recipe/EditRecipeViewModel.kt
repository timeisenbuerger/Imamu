package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository

class EditRecipeViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    internal val recipe = MutableLiveData<Recipe>()

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    fun onSaveRecipe()
    {
        recipe.value?.let {
            for (recipeIngredient in it.recipeIngredients)
            {
                if (recipeIngredient.ingredient.target.id == 0L)
                {
                    recipeIngredient.ingredient.target = ingredientRepository.getIngredientForName(recipeIngredient.ingredient.target.name)
                }
            }
            recipeRepository.save(it)
        }
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }
}