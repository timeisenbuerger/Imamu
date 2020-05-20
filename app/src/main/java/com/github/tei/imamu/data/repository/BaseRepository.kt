package com.github.tei.imamu.data.repository

import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseRepository<T>(@PublishedApi internal val boxStore: BoxStore)
{
    abstract fun getAll(): ObjectBoxLiveData<T>

    inline fun <reified T : Any> save(data: T)
    {
        CoroutineScope(Dispatchers.IO).launch {
            boxStore.boxFor<T>()
                .put(data)
        }
    }

    inline fun <reified T : Any> remove(data: T)
    {
        CoroutineScope(Dispatchers.IO).launch {
            boxStore.boxFor<T>()
                .remove(data)
        }
    }
}