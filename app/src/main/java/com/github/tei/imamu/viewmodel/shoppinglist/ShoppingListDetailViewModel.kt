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

class ShoppingListDetailViewModel(currentShoppingList: ShoppingList) : ViewModel()
{
    private var viewModelJob = Job()

    private val _shoppingList = MutableLiveData<ShoppingList>()
    val shoppingList: LiveData<ShoppingList>
        get() = _shoppingList

    private val _updateAfterDelete = MutableLiveData<Boolean>()
    val updateAfterDelete: LiveData<Boolean>
        get() = _updateAfterDelete

    private val shoppingListBox: Box<ShoppingList> = ObjectBox.boxStore.boxFor()
    private val shoppingListItemBox: Box<ShoppingListItem> = ObjectBox.boxStore.boxFor()

    init
    {
        _shoppingList.value = currentShoppingList
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun removeItem(item: ShoppingListItem?)
    {
        item?.let {
            if (shoppingList.value?.shoppingListItems!!.contains(it))
            {
                shoppingList.value?.shoppingListItems!!.remove(it)
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

    private fun onDeleteItems()
    {
        _updateAfterDelete.value = true
    }

    fun onDeleteItemsComplete()
    {
        _updateAfterDelete.value = false
    }
}