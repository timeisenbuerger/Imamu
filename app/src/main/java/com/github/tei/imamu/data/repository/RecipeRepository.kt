package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class RecipeRepository(store: BoxStore) : BaseRepository<Recipe>(store)
{
    private val recipeBox = boxStore.boxFor<Recipe>()

    override fun getAll(): ObjectBoxLiveData<Recipe>
    {
        return ObjectBoxLiveData(recipeBox.query().build())
    }
}