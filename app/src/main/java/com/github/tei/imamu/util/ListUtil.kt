package com.github.tei.imamu.util

import android.view.View.MeasureSpec
import android.widget.ListAdapter
import android.widget.ListView

fun setListViewHeightBasedOnChildren(listView: ListView) : Boolean
{
    val listAdapter: ListAdapter = listView.adapter ?: return false
    return run {
        val numberOfItems = listAdapter.count

        // Get total height of all items.
        var totalItemsHeight = 0
        for (itemPos in 0 until numberOfItems)
        {
            val item = listAdapter.getView(itemPos, null, listView)
            val px = 500 * listView.resources.displayMetrics.density
            item.measure(MeasureSpec.makeMeasureSpec(px.toInt(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            totalItemsHeight += item.measuredHeight
        }

        // Get total height of all item dividers.
        val totalDividersHeight = listView.dividerHeight * (numberOfItems - 1)
        // Get padding
        val totalPadding = listView.paddingTop + listView.paddingBottom

        // Set list height.
        val params = listView.layoutParams
        params.height = totalItemsHeight + totalDividersHeight + totalPadding
        listView.layoutParams = params
        listView.requestLayout()
        true
    }
}