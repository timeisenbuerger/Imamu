package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import de.tei.re.logic.ChefkochExtractor
import de.tei.re.logic.ChefkochRotWExtractor
import de.tei.re.logic.KitchenStoriesExtractor
import de.tei.re.logic.KitchenStoriesRotWExtractor
import de.tei.re.model.RotWTemplate

class ImportRecipeViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    private var chefKochExtractor: ChefkochExtractor
    private var kitchenStoriesExtractor: KitchenStoriesExtractor
    private lateinit var chefKochRotWExtractor: ChefkochRotWExtractor
    private lateinit var kitchenStoriesRotWExtractor: KitchenStoriesRotWExtractor

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe>
        get() = _recipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    init
    {
        _recipe.value = Recipe()
        chefKochExtractor = ChefkochExtractor()
        kitchenStoriesExtractor = KitchenStoriesExtractor()
    }

    fun startImport(url: String)
    {
        if (url.contains("chefkoch"))
        {
            chefKochExtractor.changeRecipe(url)
            createChefkochRecipe()
        }
        else if (url.contains("kitchenstories"))
        {
            kitchenStoriesExtractor.changeRecipe(url);
            createKitchenStoriesRecipe()
        }
    }

    fun retrieveChefKochRotW(): MutableList<RotWTemplate>
    {
        chefKochRotWExtractor = ChefkochRotWExtractor()
        return chefKochRotWExtractor.recipesOfTheWeek
    }

    fun retrieveKitchenStoriesRotW(): MutableList<RotWTemplate>
    {
        kitchenStoriesRotWExtractor = KitchenStoriesRotWExtractor()
        return kitchenStoriesRotWExtractor.recipesOfTheWeek
    }

    private fun createChefkochRecipe()
    {
        _recipe.value?.let { item ->
            item.title = chefKochExtractor.title
            item.preparation = chefKochExtractor.instruction
            item.servingsNumber = chefKochExtractor.portions

            for (ingredient: de.tei.re.model.RecipeIngredient in chefKochExtractor.recipeIngredients)
            {
                var recipeIngredient = RecipeIngredient()

                recipeIngredient.amount = ingredient.amount
                recipeIngredient.unit = ingredient.unit
                recipeIngredient.ingredient.target = ingredientRepository.getIngredientForName(ingredient.name)

                item.recipeIngredients.add(recipeIngredient)
            }

            for (tag in chefKochExtractor.tags)
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

    private fun createKitchenStoriesRecipe()
    {
        _recipe.value?.let {
            it.title = kitchenStoriesExtractor.title
            it.difficulty = kitchenStoriesExtractor.difficulty

            for(i in 0 until  3)
            {
                when(i)
                {
                    0 -> it.preparationTime = kitchenStoriesExtractor.times[i].filter { time -> time.isDigit() }
                    1 -> it.bakingTime = kitchenStoriesExtractor.times[i].filter { time -> time.isDigit() }
                    2 -> it.restTime = kitchenStoriesExtractor.times[i].filter { time -> time.isDigit() }
                }
            }

            it.servingsNumber = kitchenStoriesExtractor.portions

            for (step in kitchenStoriesExtractor.steps)
            {
                it.preparation += step
            }

            for (ingredient: de.tei.re.model.RecipeIngredient in kitchenStoriesExtractor.recipeIngredients)
            {
                var recipeIngredient = RecipeIngredient()

                recipeIngredient.amount = ingredient.amount
                recipeIngredient.unit = ingredient.unit
                recipeIngredient.ingredient.target = ingredientRepository.getIngredientForName(ingredient.name)

                it.recipeIngredients.add(recipeIngredient)
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