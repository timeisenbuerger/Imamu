package com.github.tei.imamu.custom.viewholder.cookbook

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookDetailRecipeListAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookDetailRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.CookBookDetailViewModel
import java.io.File

class CookBookDetailViewHolder private constructor(private val binding: ListItemCookBookDetailRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: CookBookDetailViewModel
    private lateinit var adapter: CookBookDetailRecipeListAdapter

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
        setImage(item)
        binding.textViewRecipeItem.text = item.title

        itemView.setOnClickListener {
            viewModel.onClickRecipe(item)
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
            binding.imageViewRecipeItem.setImageResource(R.drawable.ic_hot_tub)
        }
    }
}
