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
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.ChooseRecipeAdapter
import com.github.tei.imamu.databinding.FragmentAddCookBookChooseRecipesBinding
import com.github.tei.imamu.viewmodel.cookbook.choose.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.cookbook.choose.ChooseRecipeViewModelFactory

class ChooseRecipeFragment : Fragment()
{
    private lateinit var binding: FragmentAddCookBookChooseRecipesBinding
    private lateinit var viewModel: ChooseRecipeViewModel
    private lateinit var viewModelFactory: ChooseRecipeViewModelFactory
    private lateinit var application: Application
    private lateinit var listAdapter: ChooseRecipeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezepte auswählen"

        init(inflater, container)
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_cook_book_choose_recipes, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = ChooseRecipeViewModelFactory(application, ChooseRecipeFragmentArgs.fromBundle(requireArguments()).cookBook)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChooseRecipeViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = ChooseRecipeAdapter(viewModel, viewModel.recipes.value!!)
        binding.chooseRecipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.chooseRecipeList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit_search, menu)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_save_changes ->
            {
                viewModel.cookBook.value!!.recipes.clear()
                viewModel.cookBook.value!!.recipes.addAll(listAdapter.selectedItems)

                findNavController().navigate(ChooseRecipeFragmentDirections.actionChooseRecipeFragmentToAddCookBookFragment(viewModel.cookBook.value!!))
            }
        }
        return true
    }
}