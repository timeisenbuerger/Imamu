package com.github.tei.imamu.custom.viewholder.home

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.LastViewedCookBookListAdapter
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.databinding.ListItemLastViewedCookBookBinding
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import java.io.File

class LastViewedCookBookListViewHolder private constructor(private val binding: ListItemLastViewedCookBookBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: LastViewedCookBookListAdapter

    companion object
    {
        fun from(parent: ViewGroup): LastViewedCookBookListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemLastViewedCookBookBinding.inflate(layoutInflater, parent, false)

            return LastViewedCookBookListViewHolder(binding)
        }
    }

    fun bind(item: CookBook, viewModel: HomeViewModel, adapter: LastViewedCookBookListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter

        binding.cookBook = item
        binding.textViewRecipeTitle.text = item.title

        binding.chipRecipeCount.text = item.recipes.size.toString()

        binding.transparentOverlay.setOnClickListener { viewModel.onCookBookClicked(item) }

        setImage(item)
    }

    private fun setImage(item: CookBook)
    {
        for (recipe in item.recipes)
        {
            if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
            {
                binding.cardBackground.addImage(BitmapFactory.decodeFile(recipe.imagePath))
            }
            else
            {
                val bitmap = BitmapFactory.decodeResource(itemView.context.resources, R.drawable.ic_hot_tub)
                bitmap?.let {
                    binding.cardBackground.addImage(bitmap)
                }
            }
        }
    }
}
