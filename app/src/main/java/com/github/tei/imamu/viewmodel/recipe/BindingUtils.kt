package com.github.tei.imamu.viewmodel.recipe

import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.Recipe
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("recipeImage")
fun ImageView.setRecipePicture(item: Recipe)
{
    Picasso.with(context).load(File(item.imagePath)).error(R.drawable.ic_hot_tub).into(this)
}