package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.AddCookBookAdapter
import com.github.tei.imamu.databinding.FragmentAddCookbookBinding
import com.github.tei.imamu.viewmodel.cookbook.add.AddCookBookViewModel
import com.github.tei.imamu.viewmodel.cookbook.add.AddCookBookViewModelFactory

class AddCookBookFragment : Fragment()
{
    private lateinit var binding: FragmentAddCookbookBinding
    private lateinit var viewModel: AddCookBookViewModel
    private lateinit var viewModelFactory: AddCookBookViewModelFactory
    private lateinit var application: Application
    private lateinit var listAdapter: AddCookBookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Kochbuch hinzufügen"

        init(inflater, container)
        initListener()
        initObserver()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_cookbook, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = AddCookBookViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddCookBookViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = AddCookBookAdapter(viewModel)
        binding.recipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initListener()
    {

    }

    private fun initObserver()
    {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.action_save_changes -> {}
        }
        return true
    }
}