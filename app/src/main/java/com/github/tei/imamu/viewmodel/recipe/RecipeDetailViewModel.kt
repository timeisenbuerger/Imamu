package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class RecipeDetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
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

    fun onNavigateToShoppingListDetailComplete()
    {
        _navigateToShoppingListDetail.value = false
    }
}