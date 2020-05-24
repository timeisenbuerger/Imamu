package com.github.tei.imamu.view.finder

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.finder.RecipeFinderResultListAdapter
import com.github.tei.imamu.databinding.FragmentRecipeFinderResultListBinding
import com.github.tei.imamu.viewmodel.finder.RecipeFinderResultListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class RecipeFinderResultListFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeFinderResultListBinding
    private val viewModel: RecipeFinderResultListViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: RecipeFinderResultListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()
        initComponents()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_finder_result_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = RecipeFinderResultListAdapter(viewModel)
        binding.resultList.adapter = listAdapter

        val manager = LinearLayoutManager(activity)
        binding.resultList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.allRecipes = it
                listAdapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                findNavController().navigate(RecipeFinderResultListFragmentDirections.actionRecipeFinderResultListFragmentToRecipeDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    private fun initComponents()
    {
        val searchResult = RecipeFinderResultListFragmentArgs.fromBundle(requireArguments()).searchResult
        viewModel.initRecipes(searchResult)
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

    override fun onResume()
    {
        super.onResume()
        val mainActivity = (activity as MainActivity)
        mainActivity.setSupportActionBar(mainActivity.toolbar)
        mainActivity.supportActionBar?.title = "Suchergebnis"
    }
}
