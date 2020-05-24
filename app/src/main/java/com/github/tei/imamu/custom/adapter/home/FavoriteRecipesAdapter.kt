package com.github.tei.imamu.custom.adapter.home

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.ChooseRecipeViewHolder
import com.github.tei.imamu.custom.viewholder.home.FavoriteRecipesViewHolder
import com.github.tei.imamu.custom.viewholder.recipe.RecipeDiffCallback
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.viewmodel.cookbook.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.home.FavoriteRecipesViewModel
import java.util.*

class FavoriteRecipesAdapter(val viewModel: FavoriteRecipesViewModel) : ListAdapter<Recipe, FavoriteRecipesViewHolder>(RecipeDiffCallback()), Filterable
{
    lateinit var allRecipes: MutableList<Recipe>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipesViewHolder
    {
        return FavoriteRecipesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderList: FavoriteRecipesViewHolder, position: Int)
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
                    this@FavoriteRecipesAdapter.submitList(recipeList)
                }
            }
        }
    }
}