package com.github.tei.imamu.custom.viewholder.cookbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookListAdapter
import com.github.tei.imamu.databinding.ListItemLastItemActionAddBinding
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel
import com.github.tei.imamu.viewmodel.cookbook.EditCookBookViewModel

class CookBookLastItemViewHolder private constructor(private val binding: ListItemLastItemActionAddBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ViewModel

    companion object
    {
        fun from(parent: ViewGroup): CookBookLastItemViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemLastItemActionAddBinding.inflate(layoutInflater, parent, false)

            return CookBookLastItemViewHolder(binding)
        }
    }

    fun bind(viewModel: ViewModel)
    {
        this.viewModel = viewModel
        binding.buttonAdd.setOnClickListener {
            handleClick()
        }
    }

    private fun handleClick()
    {
        if (viewModel is AddCookBookViewModel)
        {
            (viewModel as AddCookBookViewModel).navigateToChooseRecipe()
        }
        else if (viewModel is EditCookBookViewModel)
        {
            (viewModel as EditCookBookViewModel).navigateToChooseRecipe()
        }
    }
}
