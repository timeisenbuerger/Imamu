package com.github.tei.imamu.custom.viewholder.shoppinglist

import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListAdapter
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.databinding.ListItemShoppingListBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel
import java.io.File

class ShoppingListViewHolder private constructor(private val binding: ListItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListAdapter

    companion object
    {
        fun from(parent: ViewGroup): ShoppingListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemShoppingListBinding.inflate(layoutInflater, parent, false)

            return ShoppingListViewHolder(binding)
        }
    }

    fun bind(item: ShoppingList, viewModel: ShoppingListViewModel, adapter: ShoppingListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.shoppingList = item
        binding.textViewShoppingListName.text = item.name
        binding.textIngredientsAmount.text = "${item.shoppingListItems.size} Zutaten"

        setImage(item)

        binding.buttonGoToItems.setOnClickListener { viewModel.onNavigateToDetail(item) }
        itemView.setOnClickListener { handleClick(item) }
        itemView.setOnLongClickListener { handleLongClick(item) }
    }

    private fun setImage(item: ShoppingList)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewShoppingList.setImageURI(Uri.parse(item.imagePath))
        }
        else
        {
            binding.imageViewShoppingList.setImageResource(R.drawable.ic_hot_tub)
        }
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
