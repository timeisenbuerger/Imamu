package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookDetailRecipeListAdapter
import com.github.tei.imamu.databinding.FragmentDetailCookBookBinding
import com.github.tei.imamu.viewmodel.cookbook.CookBookDetailViewModel
import org.koin.android.ext.android.inject

class CookBookDetailFragment : Fragment()
{
    private lateinit var binding: FragmentDetailCookBookBinding
    private val viewModel: CookBookDetailViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: CookBookDetailRecipeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        (activity as MainActivity).supportActionBar?.title = viewModel.cookBook.value!!.title

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_cook_book, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModel.cookBook.value = CookBookDetailFragmentArgs.fromBundle(requireArguments()).cookBook

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = CookBookDetailRecipeListAdapter(viewModel)
        binding.recipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.cookBook.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it.recipes)
            }
        })

        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(CookBookDetailFragmentDirections.actionCookBookDetailFragmentToRecipeDetailFragment(viewModel.clickedRecipe.value!!))
                viewModel.onNavigateToRecipeDetailComplete()
            }
        })
    }
}