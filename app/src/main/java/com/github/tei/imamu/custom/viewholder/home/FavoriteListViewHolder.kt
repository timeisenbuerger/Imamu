package com.github.tei.imamu.custom.viewholder.home

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.FavoriteListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemFavoriteRecipeBinding
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import java.io.File

class FavoriteListViewHolder private constructor(private val binding: ListItemFavoriteRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: FavoriteListAdapter

    companion object
    {
        fun from(parent: ViewGroup): FavoriteListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemFavoriteRecipeBinding.inflate(layoutInflater, parent, false)

            return FavoriteListViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: HomeViewModel, adapter: FavoriteListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        clickListener = RecipeListListener { viewModel.onRecipeClicked(item) }

        binding.recipe = item
        binding.clickListener = clickListener
        binding.textViewRecipeTitle.text = item.title

        setImage(item)
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
}
