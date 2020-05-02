package com.github.tei.imamu.custom.adapter.shoppinglist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListDiffCallback
import com.github.tei.imamu.custom.viewholder.shoppinglist.ShoppingListViewHolder
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel

class ShoppingListAdapter(val viewModel: ShoppingListViewModel, val binding: FragmentShoppingListBinding) : ListAdapter<ShoppingList, ShoppingListViewHolder>(ShoppingListDiffCallback())
{
    internal var multiSelect = false
    internal var selectedItems = mutableListOf<ShoppingList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder
    {
        return ShoppingListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: ShoppingListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this, binding)
    }
}