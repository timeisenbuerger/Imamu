package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.android.ObjectBoxLiveData

class AddRecipeViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    val recipe = MutableLiveData<Recipe>()

    private val _navigateToNextStep = MutableLiveData<Recipe>()
    val navigateToNextStep: LiveData<Recipe>
        get() = _navigateToNextStep

    private val _navigateToRecipeDetail = MutableLiveData<Recipe>()
    val navigateToRecipeDetail: LiveData<Recipe>
        get() = _navigateToRecipeDetail

    val ingredients: ObjectBoxLiveData<Ingredient> = ingredientRepository.getAll()

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
    }

    fun onNavigateToNextStep()
    {
        _navigateToNextStep.value = recipe.value
    }

    fun onNavigateToNextStepComplete()
    {
        _navigateToNextStep.value = null
    }

    fun onNavigateToDetail()
    {
        _navigateToRecipeDetail.value = recipe.value
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = null
    }
}