package com.github.tei.imamu.custom.viewholder.home

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.wrapper.ShortcutWrapper

class ShortcutDiffCallback : DiffUtil.ItemCallback<ShortcutWrapper>()
{
    override fun areItemsTheSame(oldItem: ShortcutWrapper, newItem: ShortcutWrapper): Boolean
    {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ShortcutWrapper, newItem: ShortcutWrapper): Boolean
    {
        return oldItem == newItem
    }
}