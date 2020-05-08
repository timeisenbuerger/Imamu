package com.github.tei.imamu.custom.adapter.cookbook

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.ChooseRecipeViewHolder
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookDetailViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.cookbook.choose.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.cookbook.detail.CookBookDetailViewModel

class CookBookDetailRecipeListAdapter(val viewModel: CookBookDetailViewModel) : ListAdapter<Recipe, CookBookDetailViewHolder>(RecipeDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookBookDetailViewHolder
    {
        return CookBookDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: CookBookDetailViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}