package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.data.ImamuDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.databinding.FragmentRecipeDetailBinding
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModel
import com.github.tei.imamu.viewmodel.recipe.detail.RecipeDetailViewModelFactory

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
        viewModelFactory = RecipeDetailViewModelFactory(RecipeDetailFragmentArgs.fromBundle(arguments!!).recipeId, recipeDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
    }
}