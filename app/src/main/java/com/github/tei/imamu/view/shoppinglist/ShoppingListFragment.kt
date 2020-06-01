package com.github.tei.imamu.view.shoppinglist

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListAdapter
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel
import org.koin.android.ext.android.inject

class ShoppingListFragment : Fragment()
{
    private lateinit var binding: FragmentShoppingListBinding
    private val viewModel: ShoppingListViewModel by inject()
    private lateinit var application: Application
    private lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        (activity as MainActivity).supportActionBar?.title = "Einkaufslisten"

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapters
        val manager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        shoppingListAdapter = ShoppingListAdapter(viewModel)
        binding.listParentRecipes.adapter = shoppingListAdapter
        binding.listParentRecipes.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.shoppingLists.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty())
                {
                    if (it[0].name != "Alle")
                    {
                        viewModel.addAllItemsList()
                    }

                    shoppingListAdapter.submitList(it)
                    binding.listParentRecipes.startLayoutAnimation()
                }
            }
        })

        viewModel.updateAfterDelete.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                shoppingListAdapter.submitList(viewModel.shoppingLists.value)
                viewModel.onDeleteItemsComplete()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ShoppingListFragmentDirections.actionNavShoppingListToShoppingListDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.action_add -> {
                viewModel.createNewShoppingList()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}