package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.database.entity.recipe.LastViewedRecipe
import com.github.tei.imamu.data.database.entity.recipe.LastViewedRecipe_
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor
import io.objectbox.query.LazyList

class LastViewedRecipeRepository(store: BoxStore) : BaseRepository<LastViewedRecipe>(store)
{
    internal val lastViewedRecipeBox = boxStore.boxFor<LastViewedRecipe>()

    fun getAllAsLazyList(): LazyList<LastViewedRecipe>
    {
        return lastViewedRecipeBox.query().order(LastViewedRecipe_.id).build().findLazyCached()
    }

    override fun getAll(): ObjectBoxLiveData<LastViewedRecipe>
    {
        return ObjectBoxLiveData(lastViewedRecipeBox.query().order(LastViewedRecipe_.id).build()
        )
    }
}