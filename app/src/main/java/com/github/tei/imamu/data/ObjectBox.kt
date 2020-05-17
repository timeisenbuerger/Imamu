package com.github.tei.imamu.data

import android.content.Context
import com.github.tei.imamu.data.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox
{
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context): BoxStore {
        if (::boxStore.isInitialized && !boxStore.isClosed) {
            return boxStore
        }

        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        return boxStore
    }
}