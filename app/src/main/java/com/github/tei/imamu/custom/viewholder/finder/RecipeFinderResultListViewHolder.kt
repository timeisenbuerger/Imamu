package com.github.tei.imamu.custom.viewholder.finder

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.finder.RecipeFinderResultListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemRecipeFinderSearchResultBinding
import com.github.tei.imamu.databinding.ListItemRecipeListBinding
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
        binding.textViewRecipeName.text = item.title

        setImage(item)
        initChips(item)
    }

    private fun setImage(item: Recipe)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imageViewRecipe.setImageURI(Uri.parse(item.imagePath))
        }
        else
        {
            binding.imageViewRecipe.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.imageViewRecipe.setImageResource(R.drawable.ic_hot_tub)
        }
    }

    private fun initChips(item: Recipe)
    {
        val layoutInflater = LayoutInflater.from(itemView.context)
    }
}
