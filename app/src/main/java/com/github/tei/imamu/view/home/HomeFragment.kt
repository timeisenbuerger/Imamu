package com.github.tei.imamu.view.home

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.FavoriteListAdapter
import com.github.tei.imamu.custom.adapter.home.LastViewedCookBookListAdapter
import com.github.tei.imamu.custom.adapter.home.LastViewedRecipeListAdapter
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentHomeBinding
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import org.koin.android.ext.android.inject

class HomeFragment : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by inject()
    private lateinit var application: Application
    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var lastViewedRecipeListAdapter: LastViewedRecipeListAdapter
    private lateinit var lastViewedCookBookListAdapter: LastViewedCookBookListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()
        initListener()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = viewLifecycleOwner

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        favoriteListAdapter = FavoriteListAdapter(viewModel)
        binding.favoritesList.adapter = favoriteListAdapter

        lastViewedRecipeListAdapter = LastViewedRecipeListAdapter(viewModel)
        binding.lastViewedRecipesList.adapter = lastViewedRecipeListAdapter

        lastViewedCookBookListAdapter = LastViewedCookBookListAdapter(viewModel)
        binding.lastViewedCookBooksList.adapter = lastViewedCookBookListAdapter

        val managerFavorites = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.favoritesList.layoutManager = managerFavorites

        val managerLastViewedRecipes = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.lastViewedRecipesList.layoutManager = managerLastViewedRecipes

        val managerLastViewedCookBook = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.lastViewedCookBooksList.layoutManager = managerLastViewedCookBook
    }

    private fun initObserver()
    {
        viewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoriteListAdapter.submitList(it)
                binding.favoritesList.scheduleLayoutAnimation()
            }
        })

        viewModel.lastViewedRecipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val recipes: MutableList<Recipe> = mutableListOf()
                for (lastViewedRecipe in it)
                {
                    lastViewedRecipe.recipe.target?.let {
                        recipes.add(it)
                    }
                }

                lastViewedRecipeListAdapter.submitList(recipes)
                binding.lastViewedRecipesList.scheduleLayoutAnimation()
            }
        })

        viewModel.lastViewedCookBooks.observe(viewLifecycleOwner, Observer {
            it?.let {
                val cookBooks: MutableList<CookBook> = mutableListOf()
                for (lastViewedCookBook in it)
                {
                    lastViewedCookBook.cookBook.target?.let {
                        cookBooks.add(lastViewedCookBook.cookBook.target)
                    }
                }

                lastViewedCookBookListAdapter.submitList(cookBooks)
                binding.lastViewedCookBooksList.scheduleLayoutAnimation()
            }
        })

        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToRecipeDetailFragment(recipe))
                viewModel.onNavigateToRecipeDetailComplete()
            }
        })

        viewModel.navigateToCookBookDetail.observe(viewLifecycleOwner, Observer { cookBook ->
            cookBook?.let {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToCookBookDetailFragment(cookBook))
                viewModel.onNavigateToCookBookDetailComplete()
            }
        })
    }

    private fun initListener()
    {
        binding.transparentOverlayRecipes.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavRecipeList())
        }

        binding.transparentOverlayCookBooks.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCookbook())
        }

        binding.transparentOverlayShoppingLists.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavShoppingList())
        }

        binding.transparentOverlayRecipeFinder.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavRecipeSuggestion())
        }

        binding.textViewShowAllFavorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToFavoriteRecipesFragment())
        }
    }

    override fun onResume()
    {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Startseite"
    }
}
