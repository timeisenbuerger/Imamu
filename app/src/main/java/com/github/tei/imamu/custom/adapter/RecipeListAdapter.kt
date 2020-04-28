package com.github.tei.imamu.custom.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.data.entity.Recipe
import com.github.tei.imamu.custom.viewholder.RecipeDiffCallback
import com.github.tei.imamu.custom.viewholder.RecipeListViewHolder
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModel

class RecipeListAdapter(val viewModel: RecipeListViewModel) : ListAdapter<Recipe, RecipeListViewHolder>(RecipeDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder
    {
        return RecipeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: RecipeListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }

}