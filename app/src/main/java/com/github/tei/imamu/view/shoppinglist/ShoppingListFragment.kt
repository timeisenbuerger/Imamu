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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListItemAdapter
import com.github.tei.imamu.custom.adapter.shoppinglist.ShoppingListRecipeAdapter
import com.github.tei.imamu.custom.listener.RecipeListListener
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.entity.shoppinglist.ShoppingList
import com.github.tei.imamu.databinding.FragmentShoppingListBinding
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModelFactory

class ShoppingListFragment : Fragment()
{
    private lateinit var binding: FragmentShoppingListBinding
    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var viewModelFactory: ShoppingListViewModelFactory
    private lateinit var application: Application
    private lateinit var recipeListAdapter: ShoppingListRecipeAdapter
    private lateinit var shoppingListItemAdapter: ShoppingListItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        (activity as MainActivity).supportActionBar?.title = "Einkaufslisten"

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = ShoppingListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShoppingListViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        val shoppingList: ShoppingList? = requireArguments().getSerializable("shoppingList") as ShoppingList?

        //set adapters
        val manager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recipeListAdapter = ShoppingListRecipeAdapter(viewModel, RecipeListListener { updateShoppingItemList(it) }, shoppingList?.recipe?.target)
        binding.listParentRecipes.adapter = recipeListAdapter
        binding.listParentRecipes.layoutManager = manager

        shoppingListItemAdapter = ShoppingListItemAdapter(requireContext(), mutableListOf())
        binding.listViewShoppingListIngredients.adapter = shoppingListItemAdapter
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                recipeListAdapter.submitList(it)
            }
        })
    }

    private fun updateShoppingItemList(recipe: Recipe)
    {
        val shoppingLists = viewModel.shoppingLists.value!!
        for (shoppingList in shoppingLists)
        {
            if (shoppingList.recipeId == recipe.id)
            {
                shoppingListItemAdapter.clear()
                shoppingListItemAdapter.addAll(shoppingList.shoppingListItems)

                recipeListAdapter.selectedItem = recipe

                shoppingListItemAdapter.notifyDataSetChanged()

                binding.textViewListTitle.text = shoppingList.name
                break
            }
        }
    }
}