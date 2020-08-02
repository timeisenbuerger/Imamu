package com.github.tei.imamu.view.shoppinglist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListItemAdapter
import com.github.tei.imamu.databinding.FragmentShoppingListDetailBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModel
import org.koin.android.ext.android.inject

class ShoppingListDetailFragment : Fragment()
{
    private lateinit var binding: FragmentShoppingListDetailBinding
    private val viewModel: ShoppingListDetailViewModel by inject()
    private lateinit var application: Application
    private lateinit var shoppingListItemAdapter: ShoppingListItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = viewModel.shoppingList.value?.name

        init(inflater, container)
        initObserver()
        initListener()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list_detail, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModel.shoppingList.value = ShoppingListDetailFragmentArgs.fromBundle(requireArguments()).shoppingList

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

    private fun initListener()
    {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(ShoppingListDetailFragmentDirections.actionShoppingListDetailFragmentToAddIngredientToShoppingListFragment(viewModel.shoppingList.value!!))
        }
    }

    override fun onResume()
    {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = viewModel.shoppingList.value?.name
    }
}