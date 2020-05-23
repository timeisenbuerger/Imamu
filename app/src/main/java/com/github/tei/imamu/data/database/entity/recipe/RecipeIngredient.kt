package com.github.tei.imamu.data.database.entity.recipe

import com.github.tei.imamu.data.database.entity.Ingredient
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable

@Entity
data class RecipeIngredient(
    @Id
    var id: Long = 0L,

    var amount: String = "",

    var unit: String = ""
) : Serializable
{
    lateinit var recipe: ToOne<Recipe>
    lateinit var ingredient: ToOne<Ingredient>
}