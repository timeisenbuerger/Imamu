package com.github.tei.imamu.data.database.entity.shoppinglist

import com.github.tei.imamu.data.database.entity.Ingredient
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class ShoppingListItem(
    @Id
    var id: Long = 0L,

    var amount: String = "",

    var unit: String = "",

    var isChecked: Boolean = false
)
{
    lateinit var shoppingList: ToOne<ShoppingList>
    lateinit var ingredient: ToOne<Ingredient>
}