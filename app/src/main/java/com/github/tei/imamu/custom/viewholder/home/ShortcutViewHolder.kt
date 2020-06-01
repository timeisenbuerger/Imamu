package com.github.tei.imamu.custom.viewholder.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.ItemHomeDashboardBinding
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import com.github.tei.imamu.wrapper.ShortcutWrapper

class ShortcutViewHolder private constructor(private val binding: ItemHomeDashboardBinding) : RecyclerView.ViewHolder(binding.root)
{
    private lateinit var viewModel: HomeViewModel

    companion object
    {
        fun from(parent: ViewGroup): ShortcutViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeDashboardBinding.inflate(layoutInflater, parent, false)

            return ShortcutViewHolder(binding)
        }
    }

    fun bind(item: ShortcutWrapper, viewModel: HomeViewModel)
    {
        this.viewModel = viewModel

        binding.menuText.text = item.name
        binding.shortcutView.setImageResource(item.resourceId)

         binding.transparentOverlay.setOnClickListener {
             viewModel.onShortcutClicked(item)
        }
    }
}
