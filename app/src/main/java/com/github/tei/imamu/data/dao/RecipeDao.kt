package com.github.tei.imamu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.github.tei.imamu.data.entity.Recipe

@Dao
interface RecipeDao : BaseDao<Recipe>
{
    @Query("SELECT * FROM Recipe WHERE id = :id")
    abstract fun getSingle(id: Long): Recipe

    @Query("SELECT * FROM Recipe")
    abstract fun getAll(): LiveData<List<Recipe>>
}