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
import com.github.tei.imamu.databinding.ListItemShoppingListItemBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModel

class ShoppingListItemAdapter(context: Context, private val items: MutableList<ShoppingListItem>, private val viewModel: ShoppingListDetailViewModel) : ArrayAdapter<ShoppingListItem>(context, 0, items)
{
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val item = getItem(position)
        var binding: ListItemShoppingListItemBinding? = null

        binding = if (convertView == null || binding == null)
        {
            DataBindingUtil.inflate(inflater, R.layout.list_item_shopping_list_item, parent, false)
        }
        else
        {
            DataBindingUtil.getBinding(convertView)
        }

        binding?.item = item

        initListener(binding, item)

        return binding!!.root
    }

    private fun initListener(binding: ListItemShoppingListItemBinding?, item: ShoppingListItem?)
    {
        binding?.imageButtonRemoveLine?.setOnClickListener {
            items.remove(item)
            notifyDataSetChanged()

            viewModel.removeItem(item)
        }

        binding?.checkBox?.setOnClickListener { viewModel.updateItem(item) }
    }

    override fun getItem(position: Int): ShoppingListItem?
    {
        return items[position]
    }
}