package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.entity.recipe.RecipeIngredient
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class RecipeIngredientRepository(store: BoxStore) : BaseRepository<RecipeIngredient>(store)
{
    private val recipeIngredientBox = boxStore.boxFor<RecipeIngredient>()

    override fun getAll(): ObjectBoxLiveData<RecipeIngredient>
    {
        return ObjectBoxLiveData(recipeIngredientBox.query()
            .build()
        )
    }
}