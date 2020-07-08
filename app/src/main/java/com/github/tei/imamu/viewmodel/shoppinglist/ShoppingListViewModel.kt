package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.data.repository.ShoppingListRepository
import io.objectbox.android.ObjectBoxLiveData
import org.jsoup.internal.StringUtil
import java.text.SimpleDateFormat
import java.util.*

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

    private lateinit var allShoppingList: ShoppingList

    fun deleteShoppingLists(items: MutableList<ShoppingList>)
    {
        if (items.contains(allShoppingList))
        {
            items.remove(allShoppingList)
        }

        for (item in items)
        {
            allShoppingList.shoppingListItems.removeAll(item.shoppingListItems)
        }

        _shoppingLists.value?.removeAll(items)
        for (item in items)
        {
            shoppingListRepository.remove(item)
        }

        if (_shoppingLists.value!!.size == 1 && _shoppingLists.value!![0] == allShoppingList)
        {
            _shoppingLists.value!!.remove(allShoppingList)
        }

        onDeleteItems()
    }

    fun createNewShoppingList()
    {
        val newShoppingList = ShoppingList()

        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        newShoppingList.name = "Einkaufsliste $currentDate"

        shoppingListRepository.save(newShoppingList)
        _shoppingLists.value!!.add(newShoppingList)
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

    fun addAllItemsList()
    {
        allShoppingList = ShoppingList()
        allShoppingList.name = "Alle"
        for (shoppingList in shoppingLists.value!!)
        {
            allShoppingList.shoppingListItems.addAll(shoppingList.shoppingListItems)
        }

        shoppingLists.value!!.add(0, allShoppingList)
        _shoppingLists = shoppingLists
    }
}