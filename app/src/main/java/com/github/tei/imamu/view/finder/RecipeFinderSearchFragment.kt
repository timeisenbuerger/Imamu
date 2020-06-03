package com.github.tei.imamu.view.finder

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentRecipeFinderSearchBinding
import com.github.tei.imamu.viewmodel.finder.RecipeFinderSearchViewModel
import com.google.android.material.chip.Chip
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import org.koin.android.ext.android.inject

class RecipeFinderSearchFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeFinderSearchBinding
    private val viewModel: RecipeFinderSearchViewModel by inject()
    private lateinit var application: Application
    private var chips = mutableMapOf<String, Chip>()
    private var selectedChipValues = mutableMapOf<String, String>()
    private var selectedIngredients = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()
        initListener()
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

    private fun initObserver()
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

        viewModel.navigateToSearchResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(RecipeFinderSearchFragmentDirections.actionNavRecipeSuggestionToRecipeFinderResultListFragment(it))
                viewModel.onNavigateToSearchResultComplete()
            }
        })
    }

    private fun initListener()
    {
        binding.fabStartSearch.setOnClickListener {
            collectSearchData()
            viewModel.startSearch(selectedChipValues, selectedIngredients)
        }
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        //Type of Recipe
        createChip("Vorspeise", inflater, binding.typeOfRecipe)
        createChip("Salat", inflater, binding.typeOfRecipe)
        createChip("Hauptspeise", inflater, binding.typeOfRecipe)
        createChip("Dessert", inflater, binding.typeOfRecipe)
        createChip("Getränk", inflater, binding.typeOfRecipe)

        //Recipe Feature
        createChip("Vegetarisch", inflater, binding.recipeNutrition)
        createChip("Vegan", inflater, binding.recipeNutrition)
        createChip("Laktosefrei", inflater, binding.recipeNutrition)
        createChip("Glutenfrei", inflater, binding.recipeNutrition)
        createChip("Kalorienarm", inflater, binding.recipeNutrition)

        //Recipe Difficulty
        createChip("Einfach", inflater, binding.recipeDifficulty)
        createChip("Mittel", inflater, binding.recipeDifficulty)
        createChip("Schwer", inflater, binding.recipeDifficulty)

        //Recipe Time
        createChip("≤ 20 min", inflater, binding.recipeTime)
        createChip("≤ 30 min", inflater, binding.recipeTime)
        createChip("≤ 50 min", inflater, binding.recipeTime)
        createChip("≥ 60 min", inflater, binding.recipeTime)
    }

    private fun createChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_ingredient, parent, false) as Chip
        chip.text = name
        parent.addView(chip)

        chips[name]?.let {
            chip.isChecked = it.isChecked
        }
        chips[name] = chip
    }

    private fun collectSearchData()
    {
        selectedChipValues.clear()
        selectedIngredients.clear()

        selectedChipValues["type"] = ""
        selectedChipValues["feature"] = ""
        selectedChipValues["difficulty"] = ""
        selectedChipValues["time"] = ""

        for (entry in chips)
        {
            val chip = entry.value
            if (chip.isChecked)
            {
                when (chip.parent)
                {
                    binding.typeOfRecipe     -> selectedChipValues["type"] = chip.text.toString()
                    binding.recipeNutrition  -> selectedChipValues["feature"] += chip.text.toString() + ";"
                    binding.recipeDifficulty -> selectedChipValues["difficulty"] = chip.text.toString()
                    binding.recipeTime       -> selectedChipValues["time"] = chip.text.toString()
                }
            }
        }

        selectedIngredients = binding.chipAutoComplete.chipValues
    }
}