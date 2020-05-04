package com.github.tei.imamu.util

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import androidx.palette.graphics.Palette

class TextColorUtil
{
    companion object
    {
        fun setTextColorForImage(textView: TextView, bitmap: Bitmap)
        {
            Palette.from(bitmap)
                .generate { palette ->
                    var swatch = palette!!.vibrantSwatch
                    if (swatch == null && palette.swatches.size > 0)
                    {
                        swatch = palette.swatches[0]
                    }
                    var titleTextColor: Int = Color.WHITE
                    if (swatch != null)
                    {
                        titleTextColor = swatch.titleTextColor
                    }
                    textView.setTextColor(titleTextColor)
                }
        }

        private fun getBitmap()
        {
        }
    }
}