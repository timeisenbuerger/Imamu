package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.RecipeFeature
import com.github.tei.imamu.databinding.FragmentAddRecipeStep5Binding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject

class AddRecipeStep5Fragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeStep5Binding
    private val viewModel: AddRecipeViewModel by inject()
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_step5, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        viewModel.recipe.value = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipe
    }

    private fun initListener()
    {
        binding.buttonSave.setOnClickListener {
            updateRecipeDataFromUI()
            viewModel.onSaveRecipe()
            viewModel.onNavigateToDetail()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AddRecipeStep5FragmentDirections.actionAddRecipeStep5FragmentToRecipeDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        ///Type of Recipe
        createChip("Vorspeise", inflater, binding.chipGroupRecipeType)
        createChip("Salat", inflater, binding.chipGroupRecipeType)
        createChip("Hauptspeise", inflater, binding.chipGroupRecipeType)
        createChip("Dessert", inflater, binding.chipGroupRecipeType)
        createChip("Getränk", inflater, binding.chipGroupRecipeType)

        //Recipe Feature
        createChip("Vegetarisch", inflater, binding.chipGroupNutrition)
        createChip("Vegan", inflater, binding.chipGroupNutrition)
        createChip("Laktosefrei", inflater, binding.chipGroupNutrition)
        createChip("Glutenfrei", inflater, binding.chipGroupNutrition)
        createChip("Kalorienarm", inflater, binding.chipGroupNutrition)
    }

    private fun createChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_ingredient, parent, false) as Chip
        chip.text = name
        parent.addView(chip)

        if (name == viewModel.recipe.value!!.type || recipeContainsFeature(viewModel.recipe.value!!, name))
        {
            chip.isChecked = true
        }
    }

    private fun updateRecipeDataFromUI()
    {
        for (i in 0 until binding.chipGroupRecipeType.childCount)
        {
            val chip = binding.chipGroupRecipeType.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                viewModel.recipe.value!!.type = chip.text.toString()
                break
            }
        }

        for (i in 0 until binding.chipGroupNutrition.childCount)
        {
            val chip = binding.chipGroupNutrition.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                if (!recipeContainsFeature(viewModel.recipe.value!!, chip.text.toString()))
                {
                    val newFeature = RecipeFeature(name = chip.text.toString())
                    viewModel.recipe.value!!.recipeFeatures.add(newFeature)
                }
            }
        }
    }

    private fun recipeContainsFeature(recipe: Recipe, name: String): Boolean
    {
        var result = false
        for (recipeFeature in recipe.recipeFeatures)
        {
            if (recipeFeature.name == name)
            {
                result = true
                break
            }
        }

        return result
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_close, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_close ->
            {
                findNavController().navigate(AddRecipeStep5FragmentDirections.actionAddRecipeStep5FragmentToNavRecipeList())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume()
    {
        super.onResume()
        if (viewModel.recipe.value!!.id != 0L)
        {
            (activity as MainActivity).supportActionBar?.title = "Rezept bearbeiten"
        }
        else
        {
            (activity as MainActivity).supportActionBar?.title = "Rezept hinzufügen"
        }
    }
}
