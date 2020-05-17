package com.github.tei.imamu.custom.viewholder.cookbook

import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.ChooseRecipeAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.ChooseRecipeViewModel
import java.io.File

class ChooseRecipeViewHolder private constructor(private val binding: ListItemCookBookRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ChooseRecipeViewModel
    private lateinit var adapter: ChooseRecipeAdapter

    companion object
    {
        fun from(parent: ViewGroup): ChooseRecipeViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookRecipeBinding.inflate(layoutInflater, parent, false)

            return ChooseRecipeViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: ChooseRecipeViewModel, adapter: ChooseRecipeAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.recipe = item

        setImage(item)
        binding.textViewRecipeItem.text = item.title

        itemView.setOnClickListener {
            handleClick(item)
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

    private fun handleClick(item: Recipe)
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
}
