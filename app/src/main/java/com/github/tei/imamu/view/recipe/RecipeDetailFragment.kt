package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.R
import com.github.tei.imamu.data.ImamuDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.databinding.FragmentRecipeDetailBinding
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModel
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModelFactory
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var viewModelFactory: RecipeDetailViewModelFactory
    private lateinit var application: Application
    private lateinit var recipeDao: RecipeDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false)

        //init dao
        application = requireNotNull(this.activity).application
        recipeDao = ImamuDatabase.getInstance(application).recipeDao

        //init viewModel
        viewModelFactory = RecipeDetailViewModelFactory(RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipe, recipeDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        val recipe = binding.viewModel!!.currentRecipe.value!!
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

        if(!TextUtils.isEmpty(recipe.imagePath))
        {
            Picasso.with(requireContext()).load(recipe.imagePath).error(R.drawable.ic_hot_tub).into(binding.imageRecipe)
        }
        else
        {
            Picasso.with(requireContext()).load(R.drawable.ic_hot_tub).into(binding.imageRecipe)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
    }
}