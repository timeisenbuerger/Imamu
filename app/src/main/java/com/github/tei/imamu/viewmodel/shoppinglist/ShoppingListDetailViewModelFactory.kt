package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList

class ShoppingListDetailViewModelFactory(private val shoppingList: ShoppingList) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ShoppingListDetailViewModel::class.java))
        {
            return ShoppingListDetailViewModel(shoppingList) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}