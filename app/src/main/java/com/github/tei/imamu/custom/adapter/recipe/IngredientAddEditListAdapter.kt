package com.github.tei.imamu.custom.adapter.recipe

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.recipe.IngredientAddEditListViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.IngredientDiffCallback
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel

class IngredientAddEditListAdapter(private val viewModel: AddRecipeViewModel) : ListAdapter<RecipeIngredient, IngredientAddEditListViewHolder>(IngredientDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAddEditListViewHolder
    {
        return IngredientAddEditListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: IngredientAddEditListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, this, viewModel)
    }
}