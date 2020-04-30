package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class ShoppingListViewModel() : ViewModel()
{
    private var viewModelJob = Job()

    private val _shoppingLists = MutableLiveData<MutableList<ShoppingList>>()
    val shoppingLists: LiveData<MutableList<ShoppingList>>
        get() = _shoppingLists

    private val _recipes = MutableLiveData<MutableList<Recipe>>()
    val recipes: LiveData<MutableList<Recipe>>
        get() = _recipes

    private val shoppingListBox: Box<ShoppingList> = ObjectBox.boxStore.boxFor()

    init
    {
        _shoppingLists.value = shoppingListBox.all
        initRecipes()
    }

    private fun initRecipes()
    {
        val listOfRecipes = mutableListOf<Recipe>()
        val lists = _shoppingLists.value!!

        for (shoppingList in lists)
        {
            listOfRecipes.add(shoppingList.recipe.target)
        }
        _recipes.value = listOfRecipes
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}