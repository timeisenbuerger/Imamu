package com.github.tei.imamu.custom.adapter.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.recipe.RecipeIngredient
import com.github.tei.imamu.databinding.ListItemDetailIngredientBinding

class IngredientDetailListAdapter(context: Context, private val ingredients: MutableList<RecipeIngredient>) : ArrayAdapter<RecipeIngredient>(context, 0, ingredients)
{
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val ingredient = getItem(position)
        var binding: ListItemDetailIngredientBinding? = null

        binding = if (convertView == null || binding == null)
        {
            DataBindingUtil.inflate(inflater, R.layout.list_item_detail_ingredient, parent, false)
        }
        else
        {
            DataBindingUtil.getBinding(convertView)
        }

        binding?.ingredient = ingredient

        return binding!!.root
    }

    override fun getItem(position: Int): RecipeIngredient?
    {
        return ingredients[position]
    }

}