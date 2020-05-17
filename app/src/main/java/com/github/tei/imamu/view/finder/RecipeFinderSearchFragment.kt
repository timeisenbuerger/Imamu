package com.github.tei.imamu.view.finder

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.marginEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentRecipeFinderSearchBinding
import com.github.tei.imamu.viewmodel.finder.RecipeFinderSearchViewModel
import com.google.android.material.chip.Chip
import com.hootsuite.nachos.NachoTextView
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import org.koin.android.ext.android.inject

class RecipeFinderSearchFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeFinderSearchBinding
    private val viewModel: RecipeFinderSearchViewModel by inject()
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver(inflater)
        initComponents(inflater)

        (activity as MainActivity).supportActionBar?.title = "Rezeptsuche"

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_finder_search, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initObserver(inflater: LayoutInflater)
    {
        viewModel.ingredients.observe(viewLifecycleOwner, Observer {
            it?.let {
                val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
                val suggestions = mutableListOf<String>()
                for (ingredient in it)
                {
                    if (!TextUtils.isEmpty(ingredient.name))
                    {
                        suggestions.add(ingredient.name)
                    }
                }
                adapter.addAll(suggestions)
                binding.chipAutoComplete.setAdapter(adapter)
                binding.chipAutoComplete.enableEditChipOnTouch(false, false)
                binding.chipAutoComplete.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
            }
        })
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        //Type of Recipe
        initChip("Vorspeise", inflater, binding.typeOfRecipe)
        initChip("Salat", inflater, binding.typeOfRecipe)
        initChip("Hauptspeise", inflater, binding.typeOfRecipe)
        initChip("Dessert", inflater, binding.typeOfRecipe)
        initChip("Getränk", inflater, binding.typeOfRecipe)

        //Recipe Feature
        initChip("Vegetarisch", inflater, binding.recipeFeature)
        initChip("Vegan", inflater, binding.recipeFeature)
        initChip("Laktosefrei", inflater, binding.recipeFeature)
        initChip("Glutenfrei", inflater, binding.recipeFeature)
        initChip("Kalorienarm", inflater, binding.recipeFeature)

        //Recipe Difficulty
        initChip("Einfach", inflater, binding.recipeDifficulty)
        initChip("Schwierig", inflater, binding.recipeDifficulty)
        initChip("Schnell", inflater, binding.recipeDifficulty)

        //Recipe Time
        initChip("ca. 20 min", inflater, binding.recipeTime)
        initChip("ca. 30 min", inflater, binding.recipeTime)
        initChip("ca. 50 min", inflater, binding.recipeTime)
        initChip("> 60 min", inflater, binding.recipeTime)
    }

    private fun initChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_ingredient, parent, false) as Chip
        chip.text = name
        parent.addView(chip)
    }
}