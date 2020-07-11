package com.github.tei.imamu.custom.viewholder.recipe

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.PreviewRecipeListAdapter
import com.github.tei.imamu.databinding.ListItemRecipePreviewItemBinding
import com.github.tei.imamu.view.recipe.ImportRecipeFragment
import com.github.tei.imamu.viewmodel.recipe.ImportRecipeViewModel
import com.squareup.picasso.Picasso
import de.tei.re.model.RotWTemplate
import org.jetbrains.anko.doAsync

class PreviewRecipeListViewHolder private constructor(private val binding: ListItemRecipePreviewItemBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: ImportRecipeViewModel
    private lateinit var adapter: PreviewRecipeListAdapter
    private lateinit var importRecipeFragment: ImportRecipeFragment

    companion object
    {
        fun from(parent: ViewGroup): PreviewRecipeListViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRecipePreviewItemBinding.inflate(layoutInflater, parent, false)

            return PreviewRecipeListViewHolder(binding)
        }
    }

    fun bind(item: RotWTemplate, viewModel: ImportRecipeViewModel, adapter: PreviewRecipeListAdapter, importRecipeFragment: ImportRecipeFragment)
    {
        this.viewModel = viewModel
        this.adapter = adapter
        this.importRecipeFragment = importRecipeFragment

        binding.previewRecipe = item
        binding.textViewRecipeTitle.text = item.title
        setImage(item)
        initChips(item)

        itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.recipeUrl)
            itemView.context.startActivity(intent)
        }

        binding.fabImportRecipe.setOnClickListener {
            importRecipeFragment.importRecipe(item.recipeUrl)
        }
    }

    private fun setImage(item: RotWTemplate)
    {
        item.imageUrl?.let {
            binding.cardBackground.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load(it).error(R.drawable.ic_fastfood_grey).into(binding.cardBackground)
        }
    }

    private fun initChips(item: RotWTemplate)
    {
        item.time?.let{
            binding.chipTime.text = "${it}"
            binding.chipTime.visibility = View.VISIBLE
        }

        item.difficulty?.let {
            binding.chipDifficulty.text = it
            binding.chipDifficulty.visibility = View.VISIBLE
        }
    }
}
