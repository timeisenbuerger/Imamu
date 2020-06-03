package com.github.tei.imamu.data.database.entity.recipe

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
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

    var totalTime: Int = 0,

    var type: String = "",

    var imagePath: String = "",

    var isFavorite: Boolean = false

) : Serializable
{
    @Backlink(to = "recipe")
    lateinit var recipeIngredients: ToMany<RecipeIngredient>
    lateinit var recipeFeatures: ToMany<RecipeFeature>
}