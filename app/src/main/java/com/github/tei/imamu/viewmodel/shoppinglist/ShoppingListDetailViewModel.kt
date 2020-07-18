package com.github.tei.imamu.viewmodel.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.ShoppingListItemRepository
import com.github.tei.imamu.data.repository.ShoppingListRepository
import io.objectbox.android.ObjectBoxLiveData

class ShoppingListDetailViewModel(private val shoppingListRepository: ShoppingListRepository, private val shoppingListItemRepository: ShoppingListItemRepository,val ingredientRepository: IngredientRepository) : ViewModel()
{
    internal val shoppingList = MutableLiveData<ShoppingList>()

    private val _updateAfterDelete = MutableLiveData<Boolean>()
    val updateAfterDelete: LiveData<Boolean>
        get() = _updateAfterDelete

    private var _ingredients: ObjectBoxLiveData<Ingredient> = ingredientRepository.getAll()
    val ingredients: ObjectBoxLiveData<Ingredient>
        get() = _ingredients

    fun removeItem(item: ShoppingListItem?)
    {
        item?.let {
            if (shoppingList.value?.shoppingListItems!!.contains(it))
            {
                shoppingList.value?.shoppingListItems!!.remove(it)
            }

            shoppingListItemRepository.remove(it)
        }
    }

    fun updateItem(item: ShoppingListItem?)
    {
        item?.let {
            shoppingListItemRepository.save(item)
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

    fun saveNewItem(shoppingListItem: ShoppingListItem)
    {
        shoppingListItemRepository.save(shoppingListItem)
        shoppingList.value!!.shoppingListItems.add(shoppingListItem)
    }
}