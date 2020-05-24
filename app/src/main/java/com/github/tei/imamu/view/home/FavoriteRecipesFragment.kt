package com.github.tei.imamu.view.home

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.ChooseRecipeAdapter
import com.github.tei.imamu.custom.adapter.home.FavoriteRecipesAdapter
import com.github.tei.imamu.databinding.FragmentAddCookBookChooseRecipesBinding
import com.github.tei.imamu.databinding.FragmentFavoriteRecipesBinding
import com.github.tei.imamu.viewmodel.cookbook.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.home.FavoriteRecipesViewModel
import org.koin.android.ext.android.inject

class FavoriteRecipesFragment : Fragment()
{
    private lateinit var binding: FragmentFavoriteRecipesBinding
    private val viewModel: FavoriteRecipesViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: FavoriteRecipesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Favoriten"

        init(inflater, container)
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_recipes, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = FavoriteRecipesAdapter(viewModel)
        binding.chooseRecipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.chooseRecipeList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.allRecipes = it
                listAdapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToRecipeDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action_search_view, menu)
        val item = menu.findItem(R.id.action_search)
        item?.let {
            val searchView = it.actionView as SearchView
            searchView.isIconified = false
            val textListener = object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    listAdapter.filter.filter(newText)
                    return true
                }
            }
            searchView.setOnQueryTextListener(textListener)
        }
    }
}