package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.PermissionUtil
import com.github.tei.imamu.R
import com.github.tei.imamu.data.ImamuDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.databinding.FragmentRecipeListBinding
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListAdapter
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListListener
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModel
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModelFactory

class RecipeListFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeListBinding
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var viewModelFactory: RecipeListViewModelFactory
    private lateinit var application: Application
    private lateinit var recipeDao: RecipeDao
    private lateinit var listAdapter: RecipeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)

        init(inflater, container)
        initListener()
        initObserver()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)

        //init dao
        application = requireNotNull(this.activity).application
        recipeDao = ImamuDatabase.getInstance(application).recipeDao

        //init viewModel
        viewModelFactory = RecipeListViewModelFactory(recipeDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeListViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = RecipeListAdapter(RecipeListListener { viewModel.onRecipeClicked(it) })
        binding.recipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initListener()
    {
        binding.fab.setOnClickListener {
            findNavController().navigate(RecipeListFragmentDirections.actionRecipeListFragmentToAddRecipeFragment())
        }
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                findNavController().navigate(RecipeListFragmentDirections.actionNavRecipeListToRecipeDetailFragment(recipe))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onResume()
    {
        super.onResume()

        //TODO dreckig, evtl anpassen!
        viewModel.initRecipes()
    }


}
