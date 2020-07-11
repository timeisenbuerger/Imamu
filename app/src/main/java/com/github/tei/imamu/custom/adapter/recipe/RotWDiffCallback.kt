package com.github.tei.imamu.custom.adapter.recipe

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import de.tei.re.model.RotWTemplate

class RotWDiffCallback : DiffUtil.ItemCallback<RotWTemplate>()
{
    override fun areItemsTheSame(oldItem: RotWTemplate, newItem: RotWTemplate): Boolean
    {
        return oldItem.recipeUrl == newItem.recipeUrl
    }

    override fun areContentsTheSame(oldItem: RotWTemplate, newItem: RotWTemplate): Boolean
    {
        return oldItem.equals(newItem)
    }
}