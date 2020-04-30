package com.github.tei.imamu.data.entity.shoppinglist

import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class ShoppingListItem(
    @Id
    var id: Long = 0L,

    var amount: String = "",

    var unit: String = "",

    var name: String = "",

    var isChecked: Boolean = false
)
{
    lateinit var shoppingList: ToOne<ShoppingList>
}