package com.github.tei.imamu.custom.viewholder.shoppinglist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListItemAdapter
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListRecipeAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.databinding.ListItemShoppingListRecipeBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel

class ShoppingListRecipeViewHolder private constructor(private val binding: ListItemShoppingListRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListRecipeAdapter
    private lateinit var shoppingListBinding: FragmentShoppingListBinding

    companion object
    {
        fun from(parent: ViewGroup, viewModel: ShoppingListViewModel, shoppingListBinding: FragmentShoppingListBinding): ShoppingListRecipeViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemShoppingListRecipeBinding.inflate(layoutInflater, parent, false)

            return ShoppingListRecipeViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: ShoppingListViewModel, adapter: ShoppingListRecipeAdapter, shoppingListBinding: FragmentShoppingListBinding)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        this.shoppingListBinding = shoppingListBinding

        binding.recipe = item
        binding.textViewRecipeItem.text = item.title
        binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)

        itemView.setOnClickListener { handleClick(item) }

        if (adapter.currentSelectedView == null)
        {
            handleClick(item)
        }
    }

    private fun handleClick(item: Recipe)
    {
        updateViewBackground()
        updateShoppingItemList(item)
    }

    private fun updateShoppingItemList(recipe: Recipe)
    {
        val shoppingLists = viewModel.shoppingLists.value!!
        for (shoppingList in shoppingLists)
        {
            if (shoppingList.recipeId == recipe.id)
            {
                val shoppingListItemAdapter = shoppingListBinding.listViewShoppingListIngredients.adapter as ShoppingListItemAdapter
                shoppingListItemAdapter.clear()
                shoppingListItemAdapter.addAll(shoppingList.shoppingListItems)

                shoppingListBinding.textViewListTitle.text = shoppingList.name

                shoppingListItemAdapter.notifyDataSetChanged()
                break
            }
        }
    }

    private fun updateViewBackground()
    {
        if (adapter.currentSelectedView == null)
        {
            adapter.currentSelectedView = itemView
            itemView.setBackgroundColor(Color.LTGRAY)
        }
        else
        {
            adapter.currentSelectedView?.setBackgroundColor(Color.WHITE)
            adapter.currentSelectedView = itemView
            adapter.currentSelectedView?.setBackgroundColor(Color.LTGRAY)
        }
    }
}
