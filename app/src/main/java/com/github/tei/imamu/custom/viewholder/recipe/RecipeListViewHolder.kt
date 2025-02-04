package com.github.tei.imamu.custom.viewholder.recipe

import android.net.Uri
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.RecipeListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeListBinding
import com.github.tei.imamu.viewmodel.recipe.RecipeListViewModel
import java.io.File

class RecipeListViewHolder private constructor(private val binding: ListItemRecipeListBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var adapter: RecipeListAdapter

    companion object
    {
        fun from(parent: ViewGroup): RecipeListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRecipeListBinding.inflate(layoutInflater, parent, false)

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
        binding.textViewRecipeTitle.text = item.title

        initChips(item)
        setImage(item)

        binding.cardView.setOnLongClickListener { handleLongClick(item) }
        binding.buttonShareItem.setOnClickListener { viewModel.onShareRecipe(item) }
    }

    private fun initChips(item: Recipe)
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
            binding.chipTime.chipIcon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_time_black)
            binding.chipTime.visibility = View.VISIBLE
        }
    }

    private fun setImage(item: Recipe)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.cardBackground.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.cardBackground.setImageURI(Uri.parse(item.imagePath))
        }
        else
        {
            binding.cardBackground.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.cardBackground.setImageResource(R.drawable.ic_fastfood_grey)
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
            if (adapter.selectedItems.containsValue(item))
            {
                adapter.selectedItems.remove(binding)
                binding.cardView.isChecked = false
            }
            else
            {
                adapter.selectedItems[binding] = item
                binding.cardView.isChecked = true
            }

            adapter.actionMode?.let {
                it.title = adapter.selectedItems.size.toString() + " selektiert"
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
                viewModel.deleteRecipes(adapter.selectedItems.values.toMutableList(), itemView.context)
                viewModel.initRecipes()
                mode?.finish()
            }
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean
        {
            mode?.let { adapter.actionMode = it }
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
            for (itemRecipeListBinding in adapter.selectedItems.keys)
            {
                itemRecipeListBinding.cardView.isChecked = false
            }
            adapter.multiSelect = false
            adapter.selectedItems.clear()
        }
    }
}
