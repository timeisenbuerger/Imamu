package com.github.tei.imamu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.data.entity.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class ImamuDatabase : RoomDatabase()
{
    abstract val recipeDao: RecipeDao

    companion object
    {
        @Volatile
        private var INSTANCE: ImamuDatabase? = null

        fun getInstance(context: Context): ImamuDatabase
        {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImamuDatabase::class.java,
                        "imamu_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}