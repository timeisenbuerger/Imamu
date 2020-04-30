package com.github.tei.imamu.data.entity.shoppinglist

import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.io.Serializable

@Entity
data class ShoppingList(
    @Id var id: Long = 0L,

    var recipeId: Long = 0L,

    var name: String = ""
) : Serializable
{
    lateinit var recipe: ToOne<Recipe>

    @Backlink(to = "shoppingList")
    lateinit var shoppingListItems: ToMany<ShoppingListItem>
}