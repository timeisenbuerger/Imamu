package com.github.tei.imamu.custom.adapter.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.databinding.ListItemAddIngredientBinding
import com.github.tei.imamu.util.setListViewHeightBasedOnChildren

class IngredientAddEditAdapter(context: Context, private var ingredients: MutableList<RecipeIngredient>) : ArrayAdapter<RecipeIngredient>(context, 0, ingredients)
{
    private val inflater: LayoutInflater = (context as MainActivity).layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val ingredient = getItem(position)
        var binding: ListItemAddIngredientBinding? = null

        binding = if (convertView == null || binding == null)
        {
            DataBindingUtil.inflate(inflater, R.layout.list_item_add_ingredient, parent, false)
        }
        else
        {
            DataBindingUtil.getBinding(convertView)
        }

        if (ingredient!!.ingredient.target == null)
        {
            ingredient!!.ingredient.target = Ingredient()
        }

        binding?.item = ingredient
        binding?.imageButtonRemoveLine?.setOnClickListener {
            ingredients.remove(ingredient)
            notifyDataSetChanged()
            setListViewHeightBasedOnChildren(parent as ListView)
        }

        return binding!!.root
    }

    override fun getItem(position: Int): RecipeIngredient?
    {
        return ingredients[position]
    }
}