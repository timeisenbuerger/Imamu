package com.github.tei.imamu.custom.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.home.LastViewedRecipeListViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.home.HomeViewModel

class LastViewedRecipeListAdapter(val viewModel: HomeViewModel) : ListAdapter<Recipe, LastViewedRecipeListViewHolder>(RecipeDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastViewedRecipeListViewHolder
    {
        return LastViewedRecipeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: LastViewedRecipeListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}