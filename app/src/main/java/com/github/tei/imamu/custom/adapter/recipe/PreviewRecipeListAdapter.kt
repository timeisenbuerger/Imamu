package com.github.tei.imamu.custom.adapter.recipe

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.recipe.PreviewRecipeListViewHolder
import com.github.tei.imamu.view.recipe.ImportRecipeFragment
import com.github.tei.imamu.viewmodel.recipe.ImportRecipeViewModel
import de.tei.re.model.RotWTemplate

class PreviewRecipeListAdapter(val viewModel: ImportRecipeViewModel, val importRecipeFragment: ImportRecipeFragment) : ListAdapter<RotWTemplate, PreviewRecipeListViewHolder>(RotWDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewRecipeListViewHolder
    {
        return PreviewRecipeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: PreviewRecipeListViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this, importRecipeFragment)
    }
}