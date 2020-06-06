package com.github.tei.imamu.custom.viewholder.recipe

import androidx.recyclerview.widget.DiffUtil
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient

class IngredientDiffCallback : DiffUtil.ItemCallback<RecipeIngredient>()
{
    override fun areItemsTheSame(oldItem: RecipeIngredient, newItem: RecipeIngredient): Boolean
    {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipeIngredient, newItem: RecipeIngredient): Boolean
    {
        return oldItem == newItem
    }
}