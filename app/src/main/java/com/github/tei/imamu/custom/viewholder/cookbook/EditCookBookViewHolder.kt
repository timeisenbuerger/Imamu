package com.github.tei.imamu.custom.viewholder.cookbook

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookListAdapter
import com.github.tei.imamu.custom.adapter.cookbook.EditCookBookListAdapter
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemCookBookRecipeBinding
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel
import com.github.tei.imamu.viewmodel.cookbook.EditCookBookViewModel
import java.io.File

class EditCookBookViewHolder private constructor(private val binding: ListItemCookBookRecipeBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: EditCookBookViewModel
    private lateinit var listAdapter: EditCookBookListAdapter

    companion object
    {
        fun from(parent: ViewGroup): EditCookBookViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCookBookRecipeBinding.inflate(layoutInflater, parent, false)

            return EditCookBookViewHolder(binding)
        }
    }

    fun bind(item: Recipe, viewModel: EditCookBookViewModel, listAdapter: EditCookBookListAdapter)
    {
        this.viewModel = viewModel
        this.listAdapter = listAdapter

        binding.recipe = item
        binding.textViewRecipeItem.text = item.title

        val image = File(item.imagePath)
        when
        {
            image.exists() ->
            {
                binding.imageViewRecipeItem.setImageURI(Uri.parse(item.imagePath))
            }
            else           ->
            {
                binding.imageViewRecipeItem.setImageResource(R.drawable.ic_fastfood_grey)
            }
        }

    }
}
