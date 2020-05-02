package com.github.tei.imamu.data.entity.shoppinglist

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.io.Serializable

@Entity
data class ShoppingList(
    @Id var id: Long = 0L,

    var name: String = "") : Serializable
{
    @Backlink(to = "shoppingList")
    lateinit var shoppingListItems: ToMany<ShoppingListItem>
}