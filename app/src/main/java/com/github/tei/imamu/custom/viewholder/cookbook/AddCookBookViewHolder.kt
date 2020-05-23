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
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookListAdapter
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel
import java.io.File

class AddCookBookViewHolder private constructor(private val binding: ListItemCookBookRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: AddCookBookViewModel
    private lateinit var listAdapter: AddCookBookListAdapter

    companion object
    {
        fun from(parent: ViewGroup): AddCookBookViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookRecipeBinding.inflate(layoutInflater, parent, false)

            return AddCookBookViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: AddCookBookViewModel, listAdapter: AddCookBookListAdapter)
    {
        this.viewModel = viewModel
        this.listAdapter = listAdapter

        binding.recipe = item
        binding.textViewRecipeItem.text = item.title

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
        if (listAdapter.selectedItems.contains(item))
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
        if (listAdapter.multiSelect)
        {
            if (listAdapter.selectedItems.contains(item))
            {
                listAdapter.selectedItems.remove(item)
                itemView.setBackgroundColor(Color.WHITE)
            }
            else
            {
                listAdapter.selectedItems.add(item)
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
            listAdapter.multiSelect = true
            mode?.menuInflater?.inflate(R.menu.menu_action_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?)
        {
            listAdapter.multiSelect = false
            listAdapter.selectedItems.clear()
            listAdapter.notifyDataSetChanged()
        }
    }
}
