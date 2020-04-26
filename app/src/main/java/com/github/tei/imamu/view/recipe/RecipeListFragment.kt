package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentRecipeListBinding
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListAdapter
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModel
import com.github.tei.imamu.viewmodel.recipe.list.RecipeListViewModelFactory

class RecipeListFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeListBinding
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var viewModelFactory: RecipeListViewModelFactory
    private lateinit var application: Application
    private lateinit var listAdapter: RecipeListAdapter

    private lateinit var fabOpenAnim: Animation
    private lateinit var fabCloseAnim: Animation
    private var isOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezeptliste"

        init(inflater, container)
        initComponents()
        initListener()
        initObserver()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = RecipeListViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeListViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = RecipeListAdapter(viewModel)
        binding.recipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initComponents()
    {
        fabOpenAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabCloseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
    }

    private fun initListener()
    {
        binding.mainFab.setOnClickListener {
            if (isOpen)
            {
                binding.createRecipeFab.animation = fabCloseAnim
                binding.importRecipeFab.animation = fabCloseAnim

                binding.textViewCreate.visibility = View.INVISIBLE
                binding.textViewImport.visibility = View.INVISIBLE

                isOpen = false
            }
            else
            {
                binding.createRecipeFab.animation = fabOpenAnim
                binding.importRecipeFab.animation = fabOpenAnim

                binding.textViewCreate.visibility = View.VISIBLE
                binding.textViewImport.visibility = View.VISIBLE

                isOpen = true
            }
        }

        binding.createRecipeFab.setOnClickListener {
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
