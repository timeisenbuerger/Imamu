package com.github.tei.imamu.custom.adapter.cookbook

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.ChooseRecipeViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.cookbook.choose.ChooseRecipeViewModel

class ChooseRecipeAdapter(val viewModel: ChooseRecipeViewModel) : ListAdapter<Recipe, ChooseRecipeViewHolder>(RecipeDiffCallback())
{
    internal var selectedItems = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseRecipeViewHolder
    {
        return ChooseRecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: ChooseRecipeViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}