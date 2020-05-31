package com.github.tei.imamu.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardUtil
{
    companion object
    {
        fun hideKeyboard(view: View?, context: Context)
        {
            val token = view?.rootView?.windowToken;
            token?.let { it ->
                val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it, 0)
            }
        }
    }
}