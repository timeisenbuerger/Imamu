package com.github.tei.imamu.data.entity.recipe

import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class RecipeIngredient(
    @Id
    var id: Long = 0L,

    var amount: String = "",

    var unit: String = "",

    var name: String = ""
)
{
    lateinit var recipe: ToOne<Recipe>
}