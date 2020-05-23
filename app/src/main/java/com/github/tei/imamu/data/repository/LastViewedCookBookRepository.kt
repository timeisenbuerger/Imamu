package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.database.entity.cookbook.LastViewedCookBook
import com.github.tei.imamu.data.database.entity.cookbook.LastViewedCookBook_
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor
import io.objectbox.query.LazyList

class LastViewedCookBookRepository(store: BoxStore) : BaseRepository<LastViewedCookBook>(store)
{
    internal val lastViewedCookBookBox = boxStore.boxFor<LastViewedCookBook>()

    fun getAllAsLazyList(): LazyList<LastViewedCookBook>
    {
        return lastViewedCookBookBox.query().order(LastViewedCookBook_.id).build().findLazyCached()
    }

    override fun getAll(): ObjectBoxLiveData<LastViewedCookBook>
    {
        return ObjectBoxLiveData(lastViewedCookBookBox.query().order(LastViewedCookBook_.id)
            .build()
        )
    }
}