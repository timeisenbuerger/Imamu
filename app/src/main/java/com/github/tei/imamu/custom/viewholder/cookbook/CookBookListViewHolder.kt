package com.github.tei.imamu.custom.viewholder.cookbook

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookListAdapter
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.databinding.ListItemCookBookBinding
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import java.io.File

class CookBookListViewHolder private constructor(private val binding: ListItemCookBookBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: CookBookListViewModel
    private lateinit var adapter: CookBookListAdapter

    companion object
    {
        fun from(parent: ViewGroup, context: Context): CookBookListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookBinding.inflate(layoutInflater, parent, false)

            return CookBookListViewHolder(binding, context)
        }
    }

    fun bind(item: CookBook, viewModel: CookBookListViewModel, adapter: CookBookListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.cookBook = item

        if (item.recipes.size == 1)
        {
            binding.imageCollectionViewRecipeItem.visibility = View.GONE
            binding.imageViewRecipeItem.visibility = View.VISIBLE

            val recipe = item.recipes[0]
            if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
            {
                binding.imageViewRecipeItem.setImageBitmap(BitmapFactory.decodeFile(recipe.imagePath))
            }
        }
        else
        {
            if (item.recipes.size == 2)
            {
                binding.imageCollectionViewRecipeItem.maxImagePerRow = 1
            }

            binding.imageCollectionViewRecipeItem.visibility = View.VISIBLE
            binding.imageViewRecipeItem.visibility = View.GONE

            for (recipe in item.recipes)
            {
                if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
                {
                    binding.imageCollectionViewRecipeItem.addImage(BitmapFactory.decodeFile(recipe.imagePath))
                }
            }
        }

        binding.textViewCookBookTitle.text = item.title
        binding.textViewRecipeCount.text = item.recipes.size.toString()

        binding.transparentOverlay.setOnClickListener { selectItem(item) }
        binding.transparentOverlay.setOnLongClickListener { handleLongClick(item) }
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
            viewModel.onCookBookClicked(item)
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
                viewModel.deleteCookBooks(adapter.selectedItems)
                viewModel.initCookBooks()
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
        }
    }
}
