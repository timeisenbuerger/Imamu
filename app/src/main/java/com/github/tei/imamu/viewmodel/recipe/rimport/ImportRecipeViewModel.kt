package com.github.tei.imamu.viewmodel.recipe.rimport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.Ingredient
import com.github.tei.imamu.data.entity.Ingredient_
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.recipe.RecipeIngredient
import de.tei.re.logic.ChefkochExtractor
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToOne
import kotlinx.coroutines.Job

class ImportRecipeViewModel(application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private var recipeExtractor: ChefkochExtractor

    private val recipeBox: Box<Recipe> = ObjectBox.boxStore.boxFor()
    private val ingredientBox: Box<Ingredient> = ObjectBox.boxStore.boxFor()

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
        _recipe.value?.let {item ->
            item.title = recipeExtractor.title
            item.preparation = recipeExtractor.instruction
            item.servingsNumber = recipeExtractor.portions

            for (ingredient: de.tei.re.model.RecipeIngredient in recipeExtractor.recipeIngredients)
            {
                var recipeIngredient = RecipeIngredient()

                recipeIngredient.amount = ingredient.amount
                recipeIngredient.unit = ingredient.unit
                recipeIngredient.ingredient.target = getIngredientForName(ingredient.name)

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

            recipeBox.put(item)
        }
    }

    private fun getIngredientForName(name: String): Ingredient
    {
        val ingredient = ingredientBox.query()
            .equal(Ingredient_.name, name)
            .build()
            .find()

        return if (ingredient.isEmpty())
        {
            val newIngredient = Ingredient(name = name)
            newIngredient
        }
        else
        {
            ingredient[0]
        }
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
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