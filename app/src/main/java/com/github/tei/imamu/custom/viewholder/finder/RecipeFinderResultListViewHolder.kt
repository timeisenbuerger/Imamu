package com.github.tei.imamu.custom.viewholder.finder

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.finder.RecipeFinderResultListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeFinderSearchResultBinding
import com.github.tei.imamu.viewmodel.finder.RecipeFinderResultListViewModel
import java.io.File

class RecipeFinderResultListViewHolder private constructor(private val binding: ListItemRecipeFinderSearchResultBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: RecipeFinderResultListViewModel
    private lateinit var adapter: RecipeFinderResultListAdapter

    companion object
    {
        fun from(parent: ViewGroup): RecipeFinderResultListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRecipeFinderSearchResultBinding.inflate(layoutInflater, parent, false)

            return RecipeFinderResultListViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: RecipeFinderResultListViewModel, adapter: RecipeFinderResultListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        clickListener = RecipeListListener { viewModel.onRecipeClicked(item) }

        binding.recipe = item
        binding.wrapper = viewModel.getWrapper(item)
        binding.clickListener = clickListener
        binding.textViewRecipeTitle.text = item.title

        if (item.totalTime != 0)
        {
            binding.chipTime.text = "${item.totalTime} min"
            binding.chipTime.visibility = View.VISIBLE
        }

        setImage(item)
        initChips(item)
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
            binding.cardBackground.setImageResource(R.drawable.ic_hot_tub)
        }
    }

    private fun initChips(item: Recipe)
    {
        val layoutInflater = LayoutInflater.from(itemView.context)
    }
}
