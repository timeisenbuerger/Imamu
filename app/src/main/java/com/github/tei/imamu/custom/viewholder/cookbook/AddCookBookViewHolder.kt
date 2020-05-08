package com.github.tei.imamu.custom.viewholder.cookbook

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.add.AddCookBookViewModel
import java.io.File

class AddCookBookViewHolder private constructor(private val binding: ListItemCookBookRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: AddCookBookViewModel
    private lateinit var adapter: AddCookBookAdapter

    companion object
    {
        fun from(parent: ViewGroup): AddCookBookViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookRecipeBinding.inflate(layoutInflater, parent, false)

            return AddCookBookViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: AddCookBookViewModel, adapter: AddCookBookAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.recipe = item

        val image = File(item.imagePath)
        when
        {
            image.exists() ->
            {
                binding.imageViewRecipeItem.setImageURI(Uri.parse(item.imagePath))
            }
            else           ->
            {
                binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)
            }
        }

        itemView.setOnLongClickListener { handleLongClick(item) }

        updateViewBackground(item)
    }

    private fun updateViewBackground(item: Recipe)
    {
        if (adapter.selectedItems.contains(item))
        {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    private fun handleLongClick(item: Recipe): Boolean
    {
        (itemView.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
        selectItem(item)
        return true
    }

    private fun selectItem(item: Recipe)
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
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
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
