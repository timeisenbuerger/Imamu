package com.github.tei.imamu.custom.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookDiffCallback
import com.github.tei.imamu.custom.viewholder.home.LastViewedCookBookListViewHolder
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.viewmodel.home.HomeViewModel

class LastViewedCookBookListAdapter(val viewModel: HomeViewModel) : ListAdapter<CookBook, LastViewedCookBookListViewHolder>(CookBookDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastViewedCookBookListViewHolder
    {
        return LastViewedCookBookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: LastViewedCookBookListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}