package com.github.tei.imamu.view.recipe

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.IngredientDetailListAdapter
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentRecipeDetailBinding
import com.github.tei.imamu.util.setListViewHeightBasedOnChildren
import com.github.tei.imamu.viewmodel.recipe.RecipeDetailViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.io.File
import java.text.DecimalFormat

class RecipeDetailFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private val viewModel: RecipeDetailViewModel by inject()
    private lateinit var application: Application
    private lateinit var adapter: IngredientDetailListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)
        initObserver()
        initListener()

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
        viewModel.currentRecipe.value = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipe

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
            binding.imageRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imageRecipe.setImageURI(Uri.parse(recipe.imagePath))
        }
        else
        {
            binding.imageRecipe.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.imageRecipe.setImageResource(R.drawable.ic_hot_tub)
        }

        if (!TextUtils.isEmpty(recipe.type))
        {
            createChip(recipe.type, inflater, binding.recipeFeatures)
        }
        if (!TextUtils.isEmpty(recipe.nutrition))
        {
            val nutritionOptions = recipe.nutrition.split(";").toMutableList()
            for (nutritionOption in nutritionOptions)
            {
                if (!TextUtils.isEmpty(nutritionOption))
                {
                    createChip(nutritionOption, inflater, binding.recipeFeatures)
                }
            }
        }
        if (!TextUtils.isEmpty(recipe.difficulty))
        {
            createChip(recipe.difficulty, inflater, binding.recipeFeatures)
        }
        if (!TextUtils.isEmpty(recipe.preparationTime))
        {
            createChip("Zubereitung: " + recipe.preparationTime + " min", inflater, binding.recipeFeatures)
        }
        if (!TextUtils.isEmpty(recipe.bakingTime))
        {
            createChip("Backzeit: " + recipe.bakingTime + " min", inflater, binding.recipeFeatures)
        }
        if (!TextUtils.isEmpty(recipe.restTime))
        {
            createChip("Ruhezeit: " + recipe.restTime + " min", inflater, binding.recipeFeatures)
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

    private fun createChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_recipe_feature, parent, false) as Chip
        chip.text = name
        parent.addView(chip)
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
                var amount = ingredient.amount.toFloat()
                amount = (amount / number) * increasedNumber

                ingredient.amount = DecimalFormat.getInstance()
                    .format(amount)
                    .replace(",", ".")
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
                var amount = ingredient.amount.toFloat()
                amount = (amount / number) * decreasedNumber

                ingredient.amount = DecimalFormat.getInstance()
                    .format(amount)
                    .replace(",", ".")
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

    override fun onResume()
    {
        super.onResume()
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mainActivity.supportActionBar?.title = viewModel.currentRecipe.value!!.title
        mainActivity.binding.appBar.visibility = View.GONE
    }

    override fun onStop()
    {
        super.onStop()
        val mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(mainActivity.toolbar)
        mainActivity.binding.appBar.visibility = View.VISIBLE
        NavigationUI.setupActionBarWithNavController(mainActivity, mainActivity.navController, mainActivity.appBarConfiguration)
        NavigationUI.setupWithNavController(mainActivity.binding.navView, mainActivity.navController)
    }
}