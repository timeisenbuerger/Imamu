package com.github.tei.imamu.viewmodel.recipe

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("recipeImage")
fun ImageView.setRecipePicture(item: Recipe)
{
    Picasso.with(context).load(File(item.imagePath)).error(R.drawable.ic_hot_tub).into(this)
}