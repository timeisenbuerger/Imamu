package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShoppingListViewModelFactory : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java))
        {
            return ShoppingListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}