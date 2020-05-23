package com.github.tei.imamu.custom.adapter.cookbook

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookDiffCallback
import com.github.tei.imamu.custom.viewholder.cookbook.CookBookListViewHolder
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import java.util.*

class CookBookListAdapter(val viewModel: CookBookListViewModel, val context: Context) : ListAdapter<CookBook, CookBookListViewHolder>(CookBookDiffCallback()), Filterable
{
    internal var multiSelect = false
    internal var selectedItems = mutableListOf<CookBook>()
    lateinit var allCookBooks: MutableList<CookBook>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookBookListViewHolder
    {
        return CookBookListViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holderList: CookBookListViewHolder, position: Int)
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
                val filteredList = mutableListOf<CookBook>()

                if (TextUtils.isEmpty(constraint.toString()))
                {
                    filteredList.addAll(allCookBooks)
                }
                else
                {
                    for (cookBook in allCookBooks)
                    {
                        if (cookBook.title.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())))
                        {
                            filteredList.add(cookBook)
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
                    val cookBookList = it.values as MutableList<CookBook>
                    this@CookBookListAdapter.submitList(cookBookList)
                }
            }
        }
    }
}