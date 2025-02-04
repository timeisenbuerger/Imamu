package com.github.tei.imamu.custom.adapter.shoppinglist

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.tei.imamu.R
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingListItem
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

        initCheckBox(binding)
        initListener(binding, item)

        return binding!!.root
    }

    private fun initCheckBox(binding: ListItemShoppingListItemBinding?)
    {
        binding?.let {
            strikeThrough(it.checkBox.isChecked, it.textViewIngredient)
            strikeThrough(it.checkBox.isChecked, it.textViewIngredientAmount)
            strikeThrough(it.checkBox.isChecked, it.textViewIngredientUnit)
        }
    }

    private fun initListener(binding: ListItemShoppingListItemBinding?, item: ShoppingListItem?)
    {
        binding?.imageButtonRemoveLine?.setOnClickListener {
            items.remove(item)
            notifyDataSetChanged()

            viewModel.removeItem(item)
        }

        binding?.checkBox?.setOnClickListener {
            initCheckBox(binding)
            viewModel.updateItem(item)
        }
    }

    override fun getItem(position: Int): ShoppingListItem?
    {
        return items[position]
    }

    private fun strikeThrough(enable: Boolean, textView: TextView)
    {
        textView.paintFlags = if (enable)
        {
            (textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        }
        else
        {
            textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}