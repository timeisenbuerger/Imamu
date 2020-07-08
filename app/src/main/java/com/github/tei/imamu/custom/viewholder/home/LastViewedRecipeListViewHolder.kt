package com.github.tei.imamu.custom.viewholder.home

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.LastViewedRecipeListAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemLastViewedRecipeBinding
import com.github.tei.imamu.util.RealPathUtil
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import com.squareup.picasso.Picasso
import java.io.File

class LastViewedRecipeListViewHolder private constructor(private val binding: ListItemLastViewedRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var clickListener: RecipeListListener
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: LastViewedRecipeListAdapter

    companion object
    {
        fun from(parent: ViewGroup): LastViewedRecipeListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemLastViewedRecipeBinding.inflate(layoutInflater, parent, false)

            return LastViewedRecipeListViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: HomeViewModel, adapter: LastViewedRecipeListAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        clickListener = RecipeListListener { viewModel.onRecipeClicked(item) }

        binding.recipe = item
        binding.clickListener = clickListener
        binding.textViewRecipeTitle.text = item.title

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
            binding.cardBackground.setImageResource(R.drawable.ic_fastfood_grey)
        }
    }

    private fun initChips(item: Recipe)
    {
        if (item.totalTime != 0)
        {
            binding.chipTime.text = "${item.totalTime} min"
            binding.chipTime.visibility = View.VISIBLE
        }

        if (!TextUtils.isEmpty(item.difficulty))
        {
            binding.chipDifficulty.text = item.difficulty
            binding.chipDifficulty.visibility = View.VISIBLE
        }
    }
}
