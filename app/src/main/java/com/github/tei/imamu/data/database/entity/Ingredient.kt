package com.github.tei.imamu.data.database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

@Entity
data class Ingredient(
    @Id
    var id: Long = 0L,

    var name: String = ""
) : Serializable
{
    override fun toString(): String
    {
        return name
    }
}