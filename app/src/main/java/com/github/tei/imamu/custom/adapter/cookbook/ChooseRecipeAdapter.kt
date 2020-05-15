package com.github.tei.imamu.custom.adapter.cookbook

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.ChooseRecipeViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.cookbook.choose.ChooseRecipeViewModel
import java.util.*

class ChooseRecipeAdapter(val viewModel: ChooseRecipeViewModel, private val allRecipes: MutableList<Recipe>) : ListAdapter<Recipe, ChooseRecipeViewHolder>(RecipeDiffCallback()), Filterable
{
    internal var selectedItems = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseRecipeViewHolder
    {
        return ChooseRecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: ChooseRecipeViewHolder, position: Int)
    {
        val item = getItem(position)
        holderList.bind(item, viewModel, this)
    }

    override fun getFilter(): Filter
    {
        return object : Filter()
        {
            //run on background thread
            override fun performFiltering(constraint: CharSequence?): FilterResults
            {
                val filteredList = mutableListOf<Recipe>()

                if (TextUtils.isEmpty(constraint.toString()))
                {
                    filteredList.addAll(allRecipes)
                }
                else
                {
                    for (recipe in allRecipes)
                    {
                        if (recipe.title.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())))
                        {
                            filteredList.add(recipe)
                        }
                    }
                }

                var filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults;
            }

            //run on a ui thread
            override fun publishResults(constraint: CharSequence?, results: FilterResults?)
            {
                results?.let {
                    val recipeList = it.values as MutableList<Recipe>
                    this@ChooseRecipeAdapter.submitList(recipeList)
                }
            }
        }
    }
}