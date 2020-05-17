package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class ShoppingListRepository(store: BoxStore) : BaseRepository<ShoppingList>(store)
{
    private val shoppingListBox = boxStore.boxFor<ShoppingList>()

    override fun getAll(): ObjectBoxLiveData<ShoppingList>
    {
        return ObjectBoxLiveData(shoppingListBox.query()
            .build()
        )
    }
}