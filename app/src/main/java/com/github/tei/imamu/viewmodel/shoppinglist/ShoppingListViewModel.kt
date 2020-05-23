package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.repository.ShoppingListRepository
import io.objectbox.android.ObjectBoxLiveData

class ShoppingListViewModel(private val shoppingListRepository: ShoppingListRepository) : ViewModel()
{
    private var _shoppingLists: ObjectBoxLiveData<ShoppingList> = shoppingListRepository.getAll()
    val shoppingLists: ObjectBoxLiveData<ShoppingList>
        get() = _shoppingLists

    private val _updateAfterDelete = MutableLiveData<Boolean>()
    val updateAfterDelete: LiveData<Boolean>
        get() = _updateAfterDelete

    private val _navigateToDetail = MutableLiveData<ShoppingList>()
    val navigateToDetail: LiveData<ShoppingList>
        get() = _navigateToDetail

    fun addAllItemsList()
    {
        val allShoppingList = ShoppingList()
        allShoppingList.name = "Alle"
        for (shoppingList in shoppingLists.value!!)
        {
            allShoppingList.shoppingListItems.addAll(shoppingList.shoppingListItems)
        }

        shoppingLists.value!!.add(0, allShoppingList)
        _shoppingLists = shoppingLists
    }

    fun deleteShoppingLists(items: MutableList<ShoppingList>)
    {
        if (items.contains(_shoppingLists.value!![0]))
        {
            items.remove(_shoppingLists.value!![0])
        }

        for (item in items)
        {
            _shoppingLists.value!![0].shoppingListItems.removeAll(item.shoppingListItems)
        }

        _shoppingLists.value?.removeAll(items)
        for (item in items)
        {
            shoppingListRepository.remove(item)
        }
        onDeleteItems()
    }

    private fun onDeleteItems()
    {
        _updateAfterDelete.value = true
    }

    fun onDeleteItemsComplete()
    {
        _updateAfterDelete.value = false
    }

    fun onNavigateToDetail(item: ShoppingList)
    {
        _navigateToDetail.value = item
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }
}