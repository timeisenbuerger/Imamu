package com.github.tei.imamu.custom.viewholder.shoppinglist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListRecipeAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemShoppingListRecipeBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel

class ShoppingListRecipeViewHolder private constructor(private val binding: ListItemShoppingListRecipeBinding, private val clickListener: RecipeListListener) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListRecipeAdapter

    companion object
    {
        fun from(parent: ViewGroup, clickListener: RecipeListListener): ShoppingListRecipeViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemShoppingListRecipeBinding.inflate(layoutInflater, parent, false)

            return ShoppingListRecipeViewHolder(binding, clickListener)
        }
    }

    fun bind(item: Recipe, viewModel: ShoppingListViewModel, adapter: ShoppingListRecipeAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.recipe = item
        binding.clickListener = clickListener
        binding.textViewRecipeItem.text = item.title
        binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)

        updateViewBackground(item)
    }

    private fun updateViewBackground(item: Recipe)
    {
        adapter.selectedItem?.let {
            if (it == item)
            {
                itemView.setBackgroundColor(Color.LTGRAY)
            }
            else
            {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }
    }
}
