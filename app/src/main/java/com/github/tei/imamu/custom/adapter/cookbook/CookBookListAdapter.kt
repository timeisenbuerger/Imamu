package com.github.tei.imamu.custom.adapter.cookbook

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookDiffCallback
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookListViewHolder
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.viewmodel.cookbook.list.CookBookListViewModel

class CookBookListAdapter(val viewModel: CookBookListViewModel) : ListAdapter<CookBook, CookBookListViewHolder>(CookBookDiffCallback())
{
    internal var multiSelect = false
    internal var selectedItems = mutableListOf<CookBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookBookListViewHolder
    {
        return CookBookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: CookBookListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }

}