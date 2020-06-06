package com.github.tei.imamu.custom.viewholder.home

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.FavoriteRecipesAdapter
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemFavoriteRecipeDetailBinding
import com.github.tei.imamu.viewmodel.home.FavoriteRecipesViewModel
import java.io.File

class FavoriteRecipesViewHolder private constructor(private val binding: ListItemFavoriteRecipeDetailBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: FavoriteRecipesViewModel
    private lateinit var adapter: FavoriteRecipesAdapter

    companion object
    {
        fun from(parent: ViewGroup): FavoriteRecipesViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemFavoriteRecipeDetailBinding.inflate(layoutInflater, parent, false)

            return FavoriteRecipesViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: FavoriteRecipesViewModel, adapter: FavoriteRecipesAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.recipe = item

        setImage(item)
        binding.textViewRecipeItem.text = item.title

        itemView.setOnClickListener {
            viewModel.onRecipeClicked(item)
        }
    }

    private fun setImage(item: Recipe)
    {
        if (!TextUtils.isEmpty(item.imagePath) && File(item.imagePath).exists())
        {
            binding.imageViewRecipeItem.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imageViewRecipeItem.setImageURI(Uri.parse(item.imagePath))
        }
        else
        {
            binding.imageViewRecipeItem.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.imageViewRecipeItem.setImageResource(R.drawable.ic_fastfood_grey)
        }
    }
}
