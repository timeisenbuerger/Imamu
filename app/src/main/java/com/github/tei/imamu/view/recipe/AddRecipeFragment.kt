package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.R
import com.github.tei.imamu.data.ImamuDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.databinding.FragmentAddRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.add.AddRecipeViewModel
import com.github.tei.imamu.viewmodel.recipe.add.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeBinding
    private lateinit var viewModel: AddRecipeViewModel
    private lateinit var viewModelFactory: AddRecipeViewModelFactory
    private lateinit var application: Application
    private lateinit var recipeDao: RecipeDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        binding.imageViewMeal.setImageResource(R.drawable.ic_camera)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe, container, false)

        //init dao
        application = requireNotNull(this.activity).application
        recipeDao = ImamuDatabase.getInstance(application).recipeDao

        //init viewModel
        viewModelFactory = AddRecipeViewModelFactory(recipeDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddRecipeViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initObserver()
    {
        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer { saveRecipe ->
            if (saveRecipe)
            {
                findNavController().navigate(AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeDetailFragment(viewModel.recipe.value!!.id))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)
    {
        R.id.action_save_changes ->
        {
            viewModel.onSaveRecipe()
            true
        }
        else                     ->
        {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }
    }
}
