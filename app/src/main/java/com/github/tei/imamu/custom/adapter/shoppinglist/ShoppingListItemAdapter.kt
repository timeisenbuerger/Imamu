package com.github.tei.imamu.custom.adapter.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.databinding.ListItemShoppingListBinding

class ShoppingListItemAdapter(context: Context, private val items: MutableList<ShoppingListItem>) : ArrayAdapter<ShoppingListItem>(context, 0, items)
{
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val item = getItem(position)
        var binding: ListItemShoppingListBinding? = null

        binding = if (convertView == null || binding == null)
        {
            DataBindingUtil.inflate(inflater, R.layout.list_item_shopping_list, parent, false)
        }
        else
        {
            DataBindingUtil.getBinding(convertView)
        }

        binding?.item = item

        return binding!!.root
    }

    override fun getItem(position: Int): ShoppingListItem?
    {
        return items[position]
    }

}