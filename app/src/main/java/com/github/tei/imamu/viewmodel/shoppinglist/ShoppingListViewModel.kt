package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingListItem
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class ShoppingListViewModel() : ViewModel()
{
    private var viewModelJob = Job()

    private val _shoppingLists = MutableLiveData<MutableList<ShoppingList>>()
    val shoppingLists: LiveData<MutableList<ShoppingList>>
        get() = _shoppingLists

    private val _updateAfterDelete = MutableLiveData<Boolean>()
    val updateAfterDelete: LiveData<Boolean>
        get() = _updateAfterDelete

    private val shoppingListBox: Box<ShoppingList> = ObjectBox.boxStore.boxFor()
    private val shoppingListItemBox: Box<ShoppingListItem> = ObjectBox.boxStore.boxFor()

    init
    {
        _shoppingLists.value = shoppingListBox.all
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun removeItem(item: ShoppingListItem?)
    {
        item?.let {
            for (shoppingList in shoppingLists.value!!)
            {
                if (shoppingList.shoppingListItems.contains(it))
                {
                    shoppingList.shoppingListItems.remove(it)
                    break
                }
            }

            shoppingListBox.put()
            shoppingListItemBox.remove(it)
        }
    }

    fun updateItem(item: ShoppingListItem?)
    {
        item?.let {
            shoppingListItemBox.put(item)
        }
    }

    fun deleteShoppingLists(items: MutableList<ShoppingList>)
    {
        shoppingLists.value?.removeAll(items)
        shoppingListBox.remove(items)
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
}