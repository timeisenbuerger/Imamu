package com.github.tei.imamu.custom.viewholder.shoppinglist

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList

class ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingList>()
{
    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean
    {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean
    {
        return oldItem == newItem
    }
}