package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.recipe.RecipeIngredient
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import de.tei.re.logic.ChefkochExtractor

class ImportRecipeViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    private var recipeExtractor: ChefkochExtractor

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe>
        get() = _recipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    init
    {
        _recipe.value = Recipe()
        recipeExtractor = ChefkochExtractor()
    }

    fun startImport(url: String)
    {
        recipeExtractor.changeRecipe(url)
        createRecipe()
    }

    private fun createRecipe()
    {
        _recipe.value?.let { item ->
            item.title = recipeExtractor.title
            item.preparation = recipeExtractor.instruction
            item.servingsNumber = recipeExtractor.portions

            for (ingredient: de.tei.re.model.RecipeIngredient in recipeExtractor.recipeIngredients)
            {
                var recipeIngredient = RecipeIngredient()

                recipeIngredient.amount = ingredient.amount
                recipeIngredient.unit = ingredient.unit
                recipeIngredient.ingredient.target = ingredientRepository.getIngredientForName(ingredient.name)

                item.recipeIngredients.add(recipeIngredient)
            }

            for (tag in recipeExtractor.tags)
            {
                when
                {
                    tag.contains("Arbeitszeit") ->
                    {
                        item.preparationTime = tag.filter { it.isDigit() }
                    }
                    tag.contains("Ruhezeit")    ->
                    {
                        item.restTime = tag.filter { it.isDigit() }
                    }
                    tag.contains("Backzeit")    ->
                    {
                        item.bakingTime = tag.filter { it.isDigit() }
                    }
                }
            }

            var totalTime = 0
            if (item.preparationTime.isNotEmpty())
            {
                totalTime += item.preparationTime.toInt()
            }
            if (item.bakingTime.isNotEmpty())
            {
                totalTime += item.bakingTime.toInt()
            }
            if (item.restTime.isNotEmpty())
            {
                totalTime += item.restTime.toInt()
            }
            item.totalTime = totalTime

            recipeRepository.save(item)
        }
    }

    fun onNavigateToDetail()
    {
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }
}