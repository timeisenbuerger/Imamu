package com.github.tei.imamu.data.entity.recipe

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable

@Entity
data class RecipeIngredient(
    @Id
    var id: Long = 0L,

    var amount: String = "",

    var unit: String = "",

    var name: String = ""
) : Serializable
{
    lateinit var recipe: ToOne<Recipe>
}