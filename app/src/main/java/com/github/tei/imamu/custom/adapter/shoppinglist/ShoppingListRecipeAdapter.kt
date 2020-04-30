package com.github.tei.imamu.custom.adapter.shoppinglist

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListRecipeDiffCallback
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListRecipeViewHolder
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel
import io.objectbox.relation.ToOne

class ShoppingListRecipeAdapter(val viewModel: ShoppingListViewModel, var clickListener: RecipeListListener, var recipe: Recipe?) : ListAdapter<Recipe, ShoppingListRecipeViewHolder>(ShoppingListRecipeDiffCallback())
{
    internal var selectedItem: Recipe? = recipe

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListRecipeViewHolder
    {
        return ShoppingListRecipeViewHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(holderList: ShoppingListRecipeViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }
}