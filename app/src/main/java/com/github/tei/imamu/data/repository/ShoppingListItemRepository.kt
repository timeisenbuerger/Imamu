package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.entity.shoppinglist.ShoppingListItem
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class ShoppingListItemRepository(store: BoxStore) : BaseRepository<ShoppingListItem>(store)
{
    private val shoppingListItemBox = boxStore.boxFor<ShoppingListItem>()

    override fun getAll(): ObjectBoxLiveData<ShoppingListItem>
    {
        return ObjectBoxLiveData(shoppingListItemBox.query()
            .build()
        )
    }
}