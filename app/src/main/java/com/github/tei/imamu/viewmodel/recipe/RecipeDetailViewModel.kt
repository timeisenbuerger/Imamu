package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.ObjectBox
import com.github.tei.imamu.data.database.entity.recipe.LastViewedRecipe
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.data.repository.LastViewedRecipeRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import com.github.tei.imamu.data.repository.ShoppingListRepository
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class RecipeDetailViewModel(private val recipeRepository: RecipeRepository, private val lastViewedRecipeRepository: LastViewedRecipeRepository, private val shoppingListRepository: ShoppingListRepository) : ViewModel()
{
    internal val currentRecipe = MutableLiveData<Recipe>()

    private var _navigateToEditRecipe = MutableLiveData<Recipe>()
    val navigateToEditRecipe: LiveData<Recipe>
        get() = _navigateToEditRecipe

    fun createShoppingList(recipe: Recipe)
    {
        var shoppingList = ShoppingList()
        shoppingList.name = recipe.title
        shoppingList.imagePath = recipe.imagePath

        var shoppingListItems = mutableListOf<ShoppingListItem>()
        for (ingredient in recipe.recipeIngredients)
        {
            var shoppingListItem = ShoppingListItem(amount = ingredient.amount, unit = ingredient.unit, name = ingredient.ingredient.target.name)
            shoppingListItems.add(shoppingListItem)
        }
        shoppingList.shoppingListItems.addAll(shoppingListItems)

        shoppingListRepository.save(shoppingList)
    }

    fun updateRecipe()
    {
        recipeRepository.save(currentRecipe.value!!)
    }

    fun updateLastViewed()
    {
        val lastViewed = lastViewedRecipeRepository.getAllAsLazyList()
        lastViewed.let {
            val containsRecipe = lastViewedContainsRecipe(it)
            if (it.size == 10 && !containsRecipe)
            {
                //remove oldest entry
                lastViewedRecipeRepository.remove(it.first())

                addNewLastViewedEntry()
            }
            else if (!containsRecipe)
            {
                addNewLastViewedEntry()
            }
        }
    }

    private fun addNewLastViewedEntry()
    {
        //add new entry
        val newLastViewedCookBook = LastViewedRecipe()
        newLastViewedCookBook.recipe.target = currentRecipe.value
        lastViewedRecipeRepository.save(newLastViewedCookBook)
    }

    private fun lastViewedContainsRecipe(list: List<LastViewedRecipe>): Boolean
    {
        var containsCookBook = false
        for (lastViewedCookBook in list)
        {
            if (lastViewedCookBook.recipe.target == currentRecipe.value)
            {
                containsCookBook = true
                break
            }
        }

        return containsCookBook
    }

    fun onNavigateToEditRecipe()
    {
        _navigateToEditRecipe.value = currentRecipe.value
    }

    fun onNavigateToEditRecipeComplete()
    {
        _navigateToEditRecipe.value = null
    }
}