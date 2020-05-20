package com.github.tei.imamu.data

import android.content.Context
import android.util.Log
import com.github.tei.imamu.BuildConfig
import com.github.tei.imamu.data.entity.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

object ObjectBox
{
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context): BoxStore
    {
        if (::boxStore.isInitialized && !boxStore.isClosed)
        {
            return boxStore
        }

        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()

        if (BuildConfig.DEBUG)
        {
            Log.d("ObjectBox", String.format("Using ObjectBox %s (%s)", BoxStore.getVersion(), BoxStore.getVersionNative()))
            AndroidObjectBrowser(boxStore).start(context.applicationContext);
        }

        return boxStore
    }
}