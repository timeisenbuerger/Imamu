package com.github.tei.imamu.viewmodel.recipe.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingListItem
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class RecipeDetailViewModel(recipe: Recipe) : ViewModel()
{
    private var viewModelJob = Job()

    private val _currentRecipe = MutableLiveData<Recipe>()
    val currentRecipe: LiveData<Recipe>
        get() = _currentRecipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    private val shoppingListBox: Box<ShoppingList> = ObjectBox.boxStore.boxFor()

    internal var shoppingList: MutableLiveData<ShoppingList> = MutableLiveData<ShoppingList>()

    init
    {
        _currentRecipe.value = recipe
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun createShoppingList(recipe: Recipe)
    {
        var shoppingList = ShoppingList()
        shoppingList.name = recipe.title

        var shoppingListItems = mutableListOf<ShoppingListItem>()
        for (ingredient in recipe.recipeIngredients)
        {
            var shoppingListItem = ShoppingListItem(amount = ingredient.amount, unit = ingredient.unit, name = ingredient.name)
            shoppingListItems.add(shoppingListItem)
        }
        shoppingList.shoppingListItems.addAll(shoppingListItems)

        shoppingListBox.put(shoppingList)

        this.shoppingList.value = shoppingList
        _navigateToRecipeDetail.value = true
    }
}