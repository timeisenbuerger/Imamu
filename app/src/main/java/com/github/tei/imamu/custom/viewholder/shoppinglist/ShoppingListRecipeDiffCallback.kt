package com.github.tei.imamu.custom.viewholder.shoppinglist

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.entity.recipe.Recipe

class ShoppingListRecipeDiffCallback : DiffUtil.ItemCallback<Recipe>()
{
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean
    {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean
    {
        return oldItem == newItem
    }
}