package com.github.tei.imamu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

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
    var preparation: String = "",

    @ColumnInfo(name = "difficulty")
    var difficulty: String = "",

    @ColumnInfo(name = "preparation_time")
    var preparationTime: String = "",

    @ColumnInfo(name = "baking_time")
    var bakingTime: String = "",

    @ColumnInfo(name = "rest_time")
    var restTime: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "kitchen")
    var kitchen: String = "",

    @ColumnInfo(name = "mood")
    var mood: String = "",

    @ColumnInfo(name = "image_path")
    var imagePath: String = ""
) : Serializable