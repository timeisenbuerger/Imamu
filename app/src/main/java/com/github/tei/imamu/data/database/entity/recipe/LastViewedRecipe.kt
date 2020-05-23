package com.github.tei.imamu.data.database.entity.recipe

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class LastViewedRecipe(
    @Id
    var id: Long = 0L
)
{
    lateinit var recipe: ToOne<Recipe>
}