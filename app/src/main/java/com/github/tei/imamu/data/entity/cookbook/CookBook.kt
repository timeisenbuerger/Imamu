package com.github.tei.imamu.data.entity.cookbook

import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class CookBook(
    @Id
    var id: Long = 0L,

    var title: String = ""
)
{
    lateinit var recipes: ToMany<Recipe>
}