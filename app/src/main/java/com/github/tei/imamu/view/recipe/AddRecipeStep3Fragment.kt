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
import com.github.tei.imamu.custom.adapter.recipe.IngredientAddEditListAdapter
import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.databinding.FragmentAddRecipeStep3Binding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import org.koin.android.ext.android.inject

class AddRecipeStep3Fragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeStep3Binding
    private val viewModel: AddRecipeViewModel by inject()
    private lateinit var application: Application
    private lateinit var adapter: IngredientAddEditListAdapter
    private var isEdit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezept hinzufügen"

        init(inflater, container)
        initComponents()
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_step3, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        viewModel.recipe.value = AddRecipeStep3FragmentArgs.fromBundle(requireArguments()).recipe
        isEdit = AddRecipeStep3FragmentArgs.fromBundle(requireArguments()).isEdit
    }

    private fun initComponents()
    {
        adapter = IngredientAddEditListAdapter(viewModel)
        binding.listViewIngredients.adapter = adapter
        adapter.submitList(viewModel.recipe.value!!.recipeIngredients)
    }

    private fun initListener()
    {
        binding.buttonContinue.setOnClickListener {
            viewModel.onNavigateToNextStep()
        }

        binding.buttonAddIngredient.setOnClickListener {
            val recipeIngredient = RecipeIngredient()
            recipeIngredient.ingredient.target = Ingredient()
            viewModel.recipe.value!!.recipeIngredients.add(recipeIngredient)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToNextStep.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AddRecipeStep3FragmentDirections.actionAddRecipeStep3FragmentToAddRecipeStep4Fragment(it, isEdit))
                viewModel.onNavigateToNextStepComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        if (isEdit)
        {
            inflater.inflate(R.menu.menu_save_close, menu)
        }
        else
        {
            inflater.inflate(R.menu.menu_close, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_close ->
            {
                findNavController().navigate(AddRecipeStep3FragmentDirections.actionAddRecipeStep3FragmentToNavRecipeList())
            }
            R.id.action_save  ->
            {
                viewModel.onSaveRecipe()
                findNavController().navigate(AddRecipeStep3FragmentDirections.actionAddRecipeStep3FragmentToRecipeDetailFragment(viewModel.recipe.value!!))
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
