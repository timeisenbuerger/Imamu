package com.github.tei.imamu.view.shoppinglist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListItemAdapter
import com.github.tei.imamu.databinding.FragmentShoppingListDetailBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModel
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModelFactory

class ShoppingListDetailFragment : Fragment()
{
    private lateinit var binding: FragmentShoppingListDetailBinding
    private lateinit var viewModel: ShoppingListDetailViewModel
    private lateinit var viewModelFactory: ShoppingListDetailViewModelFactory
    private lateinit var application: Application
    private lateinit var shoppingListItemAdapter: ShoppingListItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        (activity as MainActivity).supportActionBar?.title = viewModel.shoppingList.value?.name

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list_detail, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = ShoppingListDetailViewModelFactory(ShoppingListDetailFragmentArgs.fromBundle(requireArguments()).shoppingList)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShoppingListDetailViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapters
        shoppingListItemAdapter = ShoppingListItemAdapter(requireContext(), viewModel.shoppingList.value!!.shoppingListItems, viewModel)
        binding.listViewShoppingListItems.adapter = shoppingListItemAdapter
    }

    private fun initObserver()
    {
        viewModel.updateAfterDelete.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                shoppingListItemAdapter.notifyDataSetChanged()
                viewModel.onDeleteItemsComplete()
            }
        })
    }
}