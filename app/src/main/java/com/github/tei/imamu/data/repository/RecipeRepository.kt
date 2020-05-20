package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.recipe.Recipe_
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class RecipeRepository(store: BoxStore) : BaseRepository<Recipe>(store)
{
    internal val recipeBox = boxStore.boxFor<Recipe>()

    fun getAllByIds(ids: LongArray) : MutableList<Recipe>
    {
        return recipeBox.get(ids)
    }

    override fun getAll(): ObjectBoxLiveData<Recipe>
    {
        return ObjectBoxLiveData(recipeBox.query().build())
    }
}