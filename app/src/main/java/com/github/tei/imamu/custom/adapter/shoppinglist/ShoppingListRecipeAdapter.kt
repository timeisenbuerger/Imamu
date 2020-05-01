package com.github.tei.imamu.custom.adapter.shoppinglist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.listener.CompositeOnClickListener
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListRecipeDiffCallback
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListRecipeViewHolder
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel

class ShoppingListRecipeAdapter(val viewModel: ShoppingListViewModel, val binding: FragmentShoppingListBinding) : ListAdapter<Recipe, ShoppingListRecipeViewHolder>(ShoppingListRecipeDiffCallback())
{
    internal var currentSelectedView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListRecipeViewHolder
    {
        return ShoppingListRecipeViewHolder.from(parent, viewModel, binding)
    }

    override fun onBindViewHolder(holderList: ShoppingListRecipeViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this, binding)
    }
}