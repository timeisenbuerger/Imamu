package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookListAdapter
import com.github.tei.imamu.databinding.FragmentCookbookListBinding
import com.github.tei.imamu.viewmodel.cookbook.list.CookBookListViewModel
import com.github.tei.imamu.viewmodel.cookbook.list.CookBookListViewModelFactory

class CookBookListFragment : Fragment()
{
    private lateinit var binding: FragmentCookbookListBinding
    private lateinit var viewModel: CookBookListViewModel
    private lateinit var viewModelFactory: CookBookListViewModelFactory
    private lateinit var application: Application
    private lateinit var listAdapter: CookBookListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Kochbücher"

        init(inflater, container)
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = CookBookListViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CookBookListViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = CookBookListAdapter(viewModel, viewModel.cookBooks.value!!, requireContext())
        binding.cookbookList.adapter = listAdapter

        val manager = LinearLayoutManager(activity)
        binding.cookbookList.layoutManager = manager
    }

    private fun initListener()
    {
        binding.fabCreateCookbook.setOnClickListener {
            viewModel.onNavigateToAdd()
        }
    }

    private fun initObserver()
    {
        viewModel.cookBooks.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(CookBookListFragmentDirections.actionNavCookbookToCookBookDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })

        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(CookBookListFragmentDirections.actionNavCookbookToAddCookBookFragment(null))
                viewModel.onNavigateToAddComplete()
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