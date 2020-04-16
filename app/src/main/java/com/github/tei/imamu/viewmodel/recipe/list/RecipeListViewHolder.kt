package com.github.tei.imamu.viewmodel.recipe.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeBinding

class RecipeListViewHolder private constructor(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(item: Recipe, clickListener: RecipeListListener)
    {
        binding.recipe = item
        binding.clickListener = clickListener
        binding.textViewRecipeItem.text = item.title
        binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)
        binding.executePendingBindings()
    }

    companion object
    {
        fun from(parent: ViewGroup): RecipeListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)

            return RecipeListViewHolder(binding)
        }
    }
}