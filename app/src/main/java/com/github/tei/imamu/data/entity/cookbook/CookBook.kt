package com.github.tei.imamu.data.entity.cookbook

import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.io.Serializable

@Entity
data class CookBook(
    @Id
    var id: Long = 0L,

    var title: String = ""
) : Serializable
{
    lateinit var recipes: ToMany<Recipe>
}