package com.github.tei.imamu.custom.adapter.finder

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.finder.RecipeFinderResultListViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.finder.RecipeFinderResultListViewModel
import java.util.*

class RecipeFinderResultListAdapter(val viewModel: RecipeFinderResultListViewModel) : ListAdapter<Recipe, RecipeFinderResultListViewHolder>(RecipeDiffCallback()), Filterable
{
    lateinit var allRecipes: MutableList<Recipe>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeFinderResultListViewHolder
    {
        return RecipeFinderResultListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: RecipeFinderResultListViewHolder, position: Int)
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
                        if (recipe.title.toLowerCase(Locale.getDefault())
                                .contains(constraint.toString()
                                    .toLowerCase(Locale.getDefault())
                                )
                        )
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
                    this@RecipeFinderResultListAdapter.submitList(recipeList)
                }
            }
        }
    }
}