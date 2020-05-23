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
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class RecipeDetailViewModel(private val recipeRepository: RecipeRepository, private val lastViewedRecipeRepository: LastViewedRecipeRepository) : ViewModel()
{
    internal var currentRecipe = MutableLiveData<Recipe>()

    private val _navigateToShoppingListDetail = MutableLiveData<Boolean>()
    val navigateToShoppingListDetail: LiveData<Boolean>
        get() = _navigateToShoppingListDetail

    private val shoppingListBox: Box<ShoppingList> = ObjectBox.boxStore.boxFor()

    internal var shoppingList: MutableLiveData<ShoppingList> = MutableLiveData<ShoppingList>()

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

        shoppingListBox.put(shoppingList)

        this.shoppingList.value = shoppingList
        _navigateToShoppingListDetail.value = true
    }

    fun updateRecipe()
    {
        recipeRepository.save(currentRecipe.value!!)
    }

    fun onNavigateToShoppingListDetailComplete()
    {
        _navigateToShoppingListDetail.value = false
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

    private fun lastViewedContainsRecipe(list: List<LastViewedRecipe>) : Boolean
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
}