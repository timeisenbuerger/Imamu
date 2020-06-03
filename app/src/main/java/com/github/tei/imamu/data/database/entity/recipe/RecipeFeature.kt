package com.github.tei.imamu.data.database.entity.recipe

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

@Entity
data class RecipeFeature(
    @Id
    var id: Long = 0L,

    var name: String = ""
) : Serializable
{
}