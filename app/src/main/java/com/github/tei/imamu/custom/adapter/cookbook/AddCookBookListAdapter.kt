package com.github.tei.imamu.custom.adapter.cookbook

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookLastItemViewHolder
import com.github.tei.imamu.custom.viewholder.cookbook.AddCookBookViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel

class AddCookBookListAdapter(val viewModel: AddCookBookViewModel) : ListAdapter<Recipe, RecyclerView.ViewHolder>(RecipeDiffCallback())
{
    companion object
    {
        private const val VIEW_TYPE_RECIPE = 0
        private const val VIEW_TYPE_LAST_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return if (viewType == VIEW_TYPE_RECIPE)
        {
            AddCookBookViewHolder.from(parent)
        }
        else
        {
            CookBookLastItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holderList: RecyclerView.ViewHolder, position: Int)
    {
        when (VIEW_TYPE_RECIPE)
        {
            getItemViewType(position) ->
            {
                val item = getItem(position)
                (holderList as AddCookBookViewHolder).bind(item, viewModel, this)
            }
            else                      ->
            {
                (holderList as CookBookLastItemViewHolder).bind(viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        return if(position == currentList.size) VIEW_TYPE_LAST_ITEM else VIEW_TYPE_RECIPE
    }

    override fun getItemCount(): Int
    {
        return currentList.size + 1
    }
}