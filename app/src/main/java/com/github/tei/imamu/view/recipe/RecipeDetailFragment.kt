package com.github.tei.imamu.view.recipe

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.IngredientDetailListAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentRecipeDetailBinding
import com.github.tei.imamu.util.setListViewHeightBasedOnChildren
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModel
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModelFactory
import com.google.android.material.chip.Chip
import java.io.File

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var viewModelFactory: RecipeDetailViewModelFactory
    private lateinit var application: Application
    private lateinit var adapter: IngredientDetailListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)
        initObserver()
        initListener()

        (activity as MainActivity).supportActionBar?.title = viewModel.currentRecipe.value!!.title

        setHasOptionsMenu(true)
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

        adapter = IngredientDetailListAdapter(requireContext(), viewModel.currentRecipe.value!!.recipeIngredients)
        binding.listViewIngredients.adapter = adapter
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

    private fun initObserver()
    {
        viewModel.navigateToShoppingListDetail.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(RecipeDetailFragmentDirections.actionRecipeDetailFragmentToShoppingListDetailFragment(viewModel.shoppingList.value!!))
                viewModel.onNavigateToShoppingListDetailComplete()
            }
        })
    }

    private fun initListener()
    {
        binding.buttonDecreaseServings.setOnClickListener {
            viewModel.currentRecipe.value?.let {
                decreaseServings(it)
            }
        }

        binding.buttonIncreaseServings.setOnClickListener {
            viewModel.currentRecipe.value?.let {
                increaseServings(it)
            }
        }
    }

    private fun increaseServings(recipe: Recipe)
    {
        val number = recipe.servingsNumber.toInt()
        val increasedNumber = recipe.servingsNumber.toInt() + 1

        if (increasedNumber > 1)
        {
            binding.buttonDecreaseServings.isEnabled = true
        }

        recipe.servingsNumber = increasedNumber.toString()

        for (ingredient in recipe.recipeIngredients)
        {
            if (ingredient.amount.isNotEmpty() && ingredient.amount.matches("-?\\d+(\\.\\d+)?".toRegex()))
            {
                var amount: Float = ingredient.amount.toFloat()
                amount = (amount / number) * increasedNumber

                ingredient.amount = amount.toString()
            }
        }

        adapter.notifyDataSetChanged()
        binding.textServings.text = "$increasedNumber Portionen"
    }

    private fun decreaseServings(recipe: Recipe)
    {
        val number = recipe.servingsNumber.toInt()
        val decreasedNumber = recipe.servingsNumber.toInt() - 1

        if (decreasedNumber == 1)
        {
            binding.buttonDecreaseServings.isEnabled = false
        }

        recipe.servingsNumber = decreasedNumber.toString()

        for (ingredient in recipe.recipeIngredients)
        {
            if (ingredient.amount.isNotEmpty() && ingredient.amount.matches("-?\\d+(\\.\\d+)?".toRegex()))
            {
                var amount: Float = ingredient.amount.toFloat()
                amount = (amount / number) * decreasedNumber

                ingredient.amount = amount.toString()
            }
        }

        adapter.notifyDataSetChanged()
        binding.textServings.text = "$decreasedNumber Portionen"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val recipe = binding.recipe
        when (item.itemId)
        {
            R.id.action_edit         -> recipe?.let { findNavController().navigate(RecipeDetailFragmentDirections.actionRecipeDetailFragmentToEditRecipeFragment(recipe)) }
            R.id.action_shoppingList -> recipe?.let {
                viewModel.createShoppingList(recipe)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}