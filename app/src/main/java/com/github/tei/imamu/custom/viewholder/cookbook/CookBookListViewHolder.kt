package com.github.tei.imamu.custom.viewholder.cookbook

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookListAdapter
import com.github.tei.imamu.custom.listener.CookBookListListener
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.databinding.ListItemCookbookBinding
import com.github.tei.imamu.viewmodel.cookbook.list.CookBookListViewModel

class CookBookListViewHolder private constructor(private val binding: ListItemCookbookBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: CookBookListListener
    private lateinit var viewModel: CookBookListViewModel
    private lateinit var adapter: CookBookListAdapter

    companion object
    {
        fun from(parent: ViewGroup): CookBookListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookbookBinding.inflate(layoutInflater, parent, false)

            return CookBookListViewHolder(binding)
        }
    }

    fun bind(item: CookBook, viewModel: CookBookListViewModel, adapter: CookBookListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        clickListener = CookBookListListener { selectItem(item) }

        binding.cookBook = item
        binding.clickListener = clickListener
//        val sample = BitmapFactory.decodeResource(viewModel.getApplication(), R.drawable.ic_hot_tub)
//        binding.imageViewRecipeItem.addImage(sample)
//        binding.imageViewRecipeItem.addImage(sample)
//        binding.textViewCookBookTitle.text = item.title
//        binding.textViewRecipeCount.text = item.recipes.size

        itemView.setOnLongClickListener { handleLongClick(item) }

        updateViewBackground(item)
    }

    private fun updateViewBackground(item: CookBook)
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

    private fun handleLongClick(item: CookBook): Boolean
    {
        (itemView.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
        selectItem(item)
        return true
    }

    private fun selectItem(item: CookBook)
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
            viewModel.onCookBookClicked(item)
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
                viewModel.deleteRecipes(adapter.selectedItems)
                viewModel.initCookbooks()
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
