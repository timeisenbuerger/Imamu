package com.github.tei.imamu.custom.viewholder.recipe

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.RecipeListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeBinding
import com.github.tei.imamu.util.TextColorUtil
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModel
import java.io.File

class RecipeListViewHolder private constructor(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var adapter: RecipeListAdapter

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

        initChips(item, viewModel)
        setImage(item)

        itemView.setOnLongClickListener { handleLongClick(item) }

        updateViewBackground(item)
    }

    private fun initChips(item: Recipe, viewModel: RecipeListViewModel)
    {
        var time = ""
        if (!TextUtils.isEmpty(item.preparationTime))
        {
            time = item.preparationTime
        }
        if (!TextUtils.isEmpty(item.bakingTime))
        {
            time = (time.toInt() + item.bakingTime.toInt()).toString()
        }
        if (!TextUtils.isEmpty(item.restTime))
        {
            time = (time.toInt() + item.restTime.toInt()).toString()
        }

        if (!TextUtils.isEmpty(time))
        {
            binding.chipTime.text = "$time min"
            binding.chipTime.chipIcon = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.ic_time_black)
            binding.chipTime.visibility = View.VISIBLE
        }

        if (!TextUtils.isEmpty(item.difficulty))
        {
            binding.chipDifficulty.text = item.difficulty
            binding.chipDifficulty.chipIcon = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.ic_poll_black)
            binding.chipDifficulty.visibility = View.VISIBLE
        }
    }

    private fun setImage(item: Recipe)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewRecipeItem.setImageURI(Uri.parse(item.imagePath))
            val bitmap = BitmapFactory.decodeFile(item.imagePath)
            TextColorUtil.setTextColorForImage(binding.textViewRecipeItem, bitmap)
        }
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
            viewModel.onRecipeClicked(item)
        }
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback
    {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
        {
            if (item?.itemId == R.id.action_delete)
            {
                viewModel.deleteRecipes(adapter.selectedItems)
                viewModel.initRecipes()
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
