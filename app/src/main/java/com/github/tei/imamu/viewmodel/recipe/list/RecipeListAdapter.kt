package com.github.tei.imamu.viewmodel.recipe.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.data.entity.Recipe

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