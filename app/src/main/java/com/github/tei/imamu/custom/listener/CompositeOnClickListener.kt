package com.github.tei.imamu.custom.listener

import android.view.View

class CompositeOnClickListener : View.OnClickListener
{
    var listeners: MutableList<View.OnClickListener> = mutableListOf()

    fun addOnClickListener(listener: View.OnClickListener)
    {
        listeners.add(listener)
    }

    override fun onClick(v: View?)
    {
        for (listener in listeners)
        {
            listener.onClick(v)
        }
    }
}