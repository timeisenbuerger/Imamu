package com.github.tei.imamu.view.recipe

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentRecipeDetailBinding
import com.github.tei.imamu.viewmodel.recipe.detail.IngredientsListAdapter
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModel
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModelFactory
import com.github.tei.imamu.viewmodel.recipe.detail.setListViewHeightBasedOnChildren
import com.google.android.material.chip.Chip
import java.io.File

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var viewModelFactory: RecipeDetailViewModelFactory
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)

        (activity as MainActivity).supportActionBar?.title = viewModel.currentRecipe.value!!.title

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = RecipeDetailViewModelFactory(RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipe)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
        binding.recipe = viewModel.currentRecipe.value

        binding.listViewIngredients.adapter = IngredientsListAdapter(requireContext(), viewModel.currentRecipe.value!!.recipeIngredients)
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        val recipe = binding.viewModel!!.currentRecipe.value!!

        if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
        {
            binding.imageRecipe.setImageURI(Uri.parse(recipe.imagePath))
        }
        else
        {
            binding.imageRecipe.setImageResource(R.drawable.ic_hot_tub)
        }

        if (!TextUtils.isEmpty(recipe.difficulty))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = (recipe.difficulty)
            binding.chipGroupFeatures.addView(chip)
        }
        if (!TextUtils.isEmpty(recipe.kitchen))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = (recipe.kitchen)
            binding.chipGroupFeatures.addView(chip)
        }
        if (!TextUtils.isEmpty(recipe.mood))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = (recipe.mood)
            binding.chipGroupFeatures.addView(chip)
        }
        if (!TextUtils.isEmpty(recipe.preparationTime))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = ("Zubereitung: " + recipe.preparationTime + " min")
            binding.chipGroupFeatures.addView(chip)
        }
        if (!TextUtils.isEmpty(recipe.bakingTime))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = ("Backzeit: " + recipe.bakingTime + " min")
            binding.chipGroupFeatures.addView(chip)
        }
        if (!TextUtils.isEmpty(recipe.restTime))
        {
            val chip = inflater.inflate(R.layout.item_chip_recipe_feature, binding.chipGroupFeatures, false) as Chip
            chip.text = ("Ruhezeit: " + recipe.restTime + " min")
            binding.chipGroupFeatures.addView(chip)
        }

        setListViewHeightBasedOnChildren(binding.listViewIngredients)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
    }
}