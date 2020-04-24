package com.github.tei.imamu.data

import android.content.Context
import com.github.tei.imamu.data.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox
{
    lateinit var boxStore: BoxStore

    fun init(context: Context)
    {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
    }
}