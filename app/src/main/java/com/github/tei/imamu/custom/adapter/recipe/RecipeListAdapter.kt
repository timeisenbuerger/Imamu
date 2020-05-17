package com.github.tei.imamu.custom.adapter.recipe

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.custom.viewholder.recipe.RecipeListViewHolder
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.recipe.RecipeListViewModel
import java.util.*

class RecipeListAdapter(val viewModel: RecipeListViewModel) : ListAdapter<Recipe, RecipeListViewHolder>(RecipeDiffCallback()), Filterable
{
    internal var multiSelect = false
    internal var selectedItems = mutableListOf<Recipe>()
    lateinit var allRecipes: MutableList<Recipe>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder
    {
        return RecipeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: RecipeListViewHolder, position: Int)
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
                    this@RecipeListAdapter.submitList(recipeList)
                }
            }
        }
    }
}