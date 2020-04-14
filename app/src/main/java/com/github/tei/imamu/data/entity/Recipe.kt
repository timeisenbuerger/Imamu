package com.github.tei.imamu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "serving_number")
    var servingsNumber: String = "",

    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    @ColumnInfo(name = "preparation")
    var preparation: String = ""
)