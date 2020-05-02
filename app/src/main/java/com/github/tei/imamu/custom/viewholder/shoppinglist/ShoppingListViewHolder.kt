package com.github.tei.imamu.custom.viewholder.shoppinglist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListItemAdapter
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListAdapter
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.databinding.ListItemShoppingListRecipeBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel

class ShoppingListViewHolder private constructor(private val binding: ListItemShoppingListRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var shoppingListBinding: FragmentShoppingListBinding

    companion object
    {
        fun from(parent: ViewGroup): ShoppingListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemShoppingListRecipeBinding.inflate(layoutInflater, parent, false)

            return ShoppingListViewHolder(binding)
        }
    }

    fun bind(item: ShoppingList, viewModel: ShoppingListViewModel, adapter: ShoppingListAdapter, shoppingListBinding: FragmentShoppingListBinding)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        this.shoppingListBinding = shoppingListBinding

        binding.shoppingList = item
        binding.textViewRecipeItem.text = item.name

        itemView.setOnClickListener { handleClick(item) }
        itemView.setOnLongClickListener { handleLongClick(item) }
    }

    private fun handleClick(item: ShoppingList)
    {
        selectItem(item)
    }

    private fun handleLongClick(item: ShoppingList): Boolean
    {
        (itemView.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
        selectItem(item)
        return true
    }

    private fun updateShoppingItemList(item: ShoppingList)
    {
        val shoppingListItemAdapter = shoppingListBinding.listViewShoppingListIngredients.adapter as ShoppingListItemAdapter
        shoppingListItemAdapter.clear()
        shoppingListItemAdapter.addAll(item.shoppingListItems)

        shoppingListBinding.textViewListTitle.text = "Liste " + item.name

        shoppingListItemAdapter.notifyDataSetChanged()
    }

    private fun selectItem(item: ShoppingList)
    {
        if (adapter.multiSelect)
        {
            if (adapter.selectedItems.contains(item))
            {
                adapter.selectedItems.remove(item)
                itemView.setBackgroundColor(Color.WHITE)
            }
            else
            {
                adapter.selectedItems.add(item)
                itemView.setBackgroundColor(Color.LTGRAY)
            }
        }
        else
        {
            updateShoppingItemList(item)
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
                viewModel.deleteShoppingLists(adapter.selectedItems)
                mode?.finish()
            }
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            adapter.multiSelect = true
            mode?.menuInflater?.inflate(R.menu.menu_action_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?)
        {
            adapter.multiSelect = false
            adapter.selectedItems.clear()
            adapter.notifyDataSetChanged()
        }
    }
}
