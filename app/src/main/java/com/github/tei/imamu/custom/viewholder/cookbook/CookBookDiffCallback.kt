package com.github.tei.imamu.custom.viewholder.cookbook

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.database.entity.cookbook.CookBook

class CookBookDiffCallback : DiffUtil.ItemCallback<CookBook>()
{
    override fun areItemsTheSame(oldItem: CookBook, newItem: CookBook): Boolean
    {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CookBook, newItem: CookBook): Boolean
    {
        return oldItem == newItem
    }
}