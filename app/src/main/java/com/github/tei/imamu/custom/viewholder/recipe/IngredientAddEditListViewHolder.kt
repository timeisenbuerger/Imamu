package com.github.tei.imamu.custom.viewholder.recipe

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.custom.adapter.recipe.IngredientAddEditListAdapter
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.databinding.ListItemAddIngredientBinding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel

class IngredientAddEditListViewHolder private constructor(private val binding: ListItemAddIngredientBinding) : RecyclerView.ViewHolder(binding.root)
{
    companion object
    {
        fun from(parent: ViewGroup): IngredientAddEditListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAddIngredientBinding.inflate(layoutInflater, parent, false)

            return IngredientAddEditListViewHolder(binding)
        }
    }

    fun bind(item: RecipeIngredient, adapter: IngredientAddEditListAdapter, viewModel: AddRecipeViewModel, lifecycleOwner: LifecycleOwner)
    {
        binding.item = item

        binding.imageButtonRemoveLine.setOnClickListener {
            viewModel.recipe.value!!.recipeIngredients.remove(item)
            adapter.notifyDataSetChanged()
        }

        val context = itemView.context
        val arrayAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, context.resources.getStringArray(com.github.tei.imamu.R.array.ingredient_units))
        binding.autoCompleteIngredientUnit.setAdapter(arrayAdapter)

        viewModel.ingredients.observe(lifecycleOwner, Observer {
            it?.let {
                val ingredientAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, viewModel.ingredients.value!!)
                binding.editTextIngredient.setAdapter(ingredientAdapter)
            }
        })
    }
}
