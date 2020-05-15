package com.github.tei.imamu.data.entity

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
}