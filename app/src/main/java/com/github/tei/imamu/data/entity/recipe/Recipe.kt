package com.github.tei.imamu.data.entity.recipe

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.io.Serializable

@Entity
data class Recipe(
    @Id
    var id: Long = 0L,

    var title: String = "",

    var servingsNumber: String = "",

    var preparation: String = "",

    var difficulty: String = "",

    var preparationTime: String = "",

    var bakingTime: String = "",

    var restTime: String = "",

    var type: String = "",

    var nutrition: String = "",

    var imagePath: String = ""

) : Serializable
{
    @Backlink(to = "recipe")
    lateinit var recipeIngredients: ToMany<RecipeIngredient>
}