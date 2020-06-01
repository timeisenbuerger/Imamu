package com.github.tei.imamu.custom.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.home.ShortcutDiffCallback
import com.github.tei.imamu.custom.viewholder.home.ShortcutViewHolder
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import com.github.tei.imamu.wrapper.ShortcutWrapper

class ShortcutAdapter(val viewModel: HomeViewModel) : ListAdapter<ShortcutWrapper, ShortcutViewHolder>(ShortcutDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder
    {
        return ShortcutViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: ShortcutViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel)
    }
}