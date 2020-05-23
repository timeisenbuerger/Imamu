package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class CookBookRepository(store: BoxStore) : BaseRepository<CookBook>(store)
{
    private val cookBookBox = store.boxFor<CookBook>()

    override fun getAll(): ObjectBoxLiveData<CookBook>
    {
        return ObjectBoxLiveData(cookBookBox.query().build())
    }
}