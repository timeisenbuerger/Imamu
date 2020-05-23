package com.github.tei.imamu.custom.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.home.FavoriteListViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.home.HomeViewModel

class FavoriteListAdapter(val viewModel: HomeViewModel) : ListAdapter<Recipe, FavoriteListViewHolder>(RecipeDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder
    {
        return FavoriteListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: FavoriteListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}