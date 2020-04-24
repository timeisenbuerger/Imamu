package com.github.tei.imamu.viewmodel.recipe.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeBinding

class RecipeListViewHolder private constructor(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var adapter: RecipeListAdapter

    private var multiSelect = false
    private var selectedItems = ArrayList<Recipe>()

    companion object
    {
        fun from(parent: ViewGroup): RecipeListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)

            return RecipeListViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: RecipeListViewModel, adapter: RecipeListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        clickListener = RecipeListListener { selectItem(item) }

        binding.recipe = item
        binding.clickListener = clickListener
        binding.textViewRecipeItem.text = item.title
        binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)

        itemView.setOnLongClickListener { handleLongClick(item) }

        updateViewBackground(item)
    }

    private fun updateViewBackground(item: Recipe)
    {
        if (selectedItems.contains(item))
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
        if (multiSelect)
        {
            if (selectedItems.contains(item))
            {
                selectedItems.remove(item)
                itemView.setBackgroundColor(Color.WHITE)
            }
            else
            {
                selectedItems.add(item)
                itemView.setBackgroundColor(Color.LTGRAY)
            }
        }
        else
        {
            viewModel.onRecipeClicked(item)
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
                viewModel.deleteRecipes(selectedItems)
                viewModel.initRecipes()
                mode?.finish()
            }
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            multiSelect = true
            mode?.menuInflater?.inflate(R.menu.menu_action_recipe_list, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?)
        {
            multiSelect = false
            selectedItems.clear()
            adapter.notifyDataSetChanged()
        }
    }
}
