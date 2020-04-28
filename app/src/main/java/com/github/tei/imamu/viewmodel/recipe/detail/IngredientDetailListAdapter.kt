package com.github.tei.imamu.viewmodel.recipe.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.RecipeIngredient

class IngredientDetailListAdapter(private val context: Context, private val data: List<RecipeIngredient>) : BaseAdapter()
{
    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        var item = data[position]

        val view = inflater.inflate(R.layout.list_item_ingredient, parent, false)
        val txtAmount = view.findViewById<TextView>(R.id.text_view_amount)
        val txtUnit = view.findViewById<TextView>(R.id.text_view_unit)
        val txtIngredient = view.findViewById<TextView>(R.id.text_view_ingredient)

        txtAmount.text = item.amount
        txtUnit.text = item.unit
        txtIngredient.text = item.name

        return view
    }

    override fun getItem(position: Int): Any
    {
        return position
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    override fun getCount(): Int
    {
        return data.size
    }

}