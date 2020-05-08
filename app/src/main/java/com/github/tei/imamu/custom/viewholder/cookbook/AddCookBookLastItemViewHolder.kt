package com.github.tei.imamu.custom.viewholder.cookbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ListItemLastItemActionAddBinding
import com.github.tei.imamu.viewmodel.cookbook.add.AddCookBookViewModel

class AddCookBookLastItemViewHolder private constructor(private val binding: ListItemLastItemActionAddBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: AddCookBookViewModel
    private lateinit var adapter: AddCookBookAdapter

    companion object
    {
        fun from(parent: ViewGroup): AddCookBookLastItemViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemLastItemActionAddBinding.inflate(layoutInflater, parent, false)

            return AddCookBookLastItemViewHolder(binding)
        }
    }

    fun bind(viewModel: AddCookBookViewModel, adapter: AddCookBookAdapter)
    {
        this.viewModel = viewModel
        this.adapter = adapter
    }
}
