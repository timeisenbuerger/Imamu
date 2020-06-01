package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.EditCookBookListAdapter
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.databinding.FragmentEditCookBookBinding
import com.github.tei.imamu.viewmodel.cookbook.EditCookBookViewModel
import org.koin.android.ext.android.inject

class EditCookBookFragment : Fragment()
{
    private lateinit var binding: FragmentEditCookBookBinding
    private lateinit var application: Application
    private lateinit var listListAdapter: EditCookBookListAdapter
    private val viewModel: EditCookBookViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Kochbuch bearbeiten"

        init(inflater, container)
        initObserver()
        initComponents()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_cook_book, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        viewModel.cookBook.value = EditCookBookFragmentArgs.fromBundle(requireArguments()).cookBook

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listListAdapter = EditCookBookListAdapter(viewModel)
        binding.recipeList.adapter = listListAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.navigateToChooseRecipe.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(EditCookBookFragmentDirections.actionEditCookBookFragmentToChooseRecipeFragment(viewModel.cookBook.value!!))
                viewModel.navigateToChooseRecipeComplete()
            }
        })

        viewModel.navigateToCookBookList.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(EditCookBookFragmentDirections.actionEditCookBookFragmentToCookBookDetailFragment(viewModel.cookBook.value!!))
                viewModel.navigateToCookBookListComplete()
            }
        })
    }

    private fun initComponents()
    {
        val cookBook = requireArguments().getSerializable("cookBook")
        cookBook?.let {
            viewModel.cookBook.value = it as CookBook
        }

        listListAdapter.submitList(viewModel.cookBook.value!!.recipes)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_save_changes ->
            {
                viewModel.saveCookBook()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}