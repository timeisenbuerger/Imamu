package com.github.tei.imamu.custom.viewholder.shoppinglist

import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListAdapter
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingList
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

        init(item, viewModel)
    }

    private fun init(item: ShoppingList, viewModel: ShoppingListViewModel)
    {
        binding.shoppingList = item
        binding.textViewShoppingListName.text = item.name
        binding.textIngredientsAmount.text = "${item.shoppingListItems.size} Zutaten"

        setImage(item)

        binding.cardView.setOnClickListener { selectItem(item) }
        binding.cardView.setOnLongClickListener { handleLongClick(item) }
    }

    private fun setImage(item: ShoppingList)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewShoppingList.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imageViewShoppingList.setImageURI(Uri.parse(item.imagePath))
        }
        else
        {
            binding.imageViewShoppingList.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.imageViewShoppingList.setImageResource(R.drawable.ic_fastfood_grey)
        }
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
                binding.cardView.isChecked = false
            }
            else
            {
                adapter.selectedItems.add(item)
                binding.cardView.isChecked = true
            }

            adapter.actionMode?.let {
                it.title = adapter.selectedItems.size.toString() + " selektiert"
            }
        }
        else
        {
            viewModel.onNavigateToDetail(item)
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
            mode?.let {
                adapter.actionMode = it
            }

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
            binding.cardView.isChecked = false
            adapter.multiSelect = false
            adapter.selectedItems.clear()
            adapter.notifyDataSetChanged()
        }
    }
}
