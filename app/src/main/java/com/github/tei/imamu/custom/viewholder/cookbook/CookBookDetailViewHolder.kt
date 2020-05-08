package com.github.tei.imamu.custom.viewholder.cookbook

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.custom.adapter.cookbook.CookBookDetailRecipeListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookDetailRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.detail.CookBookDetailViewModel
import java.io.File

class CookBookDetailViewHolder private constructor(private val binding: ListItemCookBookDetailRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: CookBookDetailViewModel
    private lateinit var adapter: CookBookDetailRecipeListAdapter
    private lateinit var clickListener: RecipeListListener

    companion object
    {
        fun from(parent: ViewGroup): CookBookDetailViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookDetailRecipeBinding.inflate(layoutInflater, parent, false)

            return CookBookDetailViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: CookBookDetailViewModel, adapter: CookBookDetailRecipeListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.recipe = item
        clickListener = RecipeListListener { viewModel.onClickRecipe(item) }
        setImage(item)
        binding.textViewRecipeItem.text = item.title

    }

    private fun setImage(item: Recipe)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewRecipeItem.setImageURI(Uri.parse(item.imagePath))
        }
    }
}
