package com.github.tei.imamu.view.shoppinglist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.data.database.entity.shoppinglist.ShoppingListItem
import com.github.tei.imamu.databinding.FragmentAddIngredientToShoppingListBinding
import com.github.tei.imamu.util.KeyboardUtil
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModel
import org.koin.android.ext.android.inject

class AddIngredientToShoppingListFragment : Fragment()
{
    private lateinit var binding: FragmentAddIngredientToShoppingListBinding
    private val viewModel: ShoppingListDetailViewModel by inject()
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Zutaten hinzufügen"

        init(inflater, container)
        initComponents()
        initObserver()
        initListener()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_ingredient_to_shopping_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModel.shoppingList.value = AddIngredientToShoppingListFragmentArgs.fromBundle(requireArguments()).shoppingList

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initComponents()
    {
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, requireContext().resources.getStringArray(R.array.ingredient_units))
        binding.autoCompleteUnit.setAdapter(arrayAdapter)
    }

    private fun initObserver()
    {
        viewModel.ingredients.observe(viewLifecycleOwner, Observer {
            it?.let {
                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
                binding.autoCompleteIngredient.setAdapter(arrayAdapter)
            }
        })
    }

    private fun initListener()
    {
        binding.buttonAddIngredient.setOnClickListener {
            KeyboardUtil.hideKeyboard(view, requireContext())
            addNewIngredient()

            Toast.makeText(requireContext(), "Zutat wurde hinzugefügt", Toast.LENGTH_SHORT).show()

            binding.editTextAmount.setText("")
            binding.autoCompleteUnit.setText("")
            binding.autoCompleteIngredient.setText("")
        }

        binding.fabFinish.setOnClickListener {
            KeyboardUtil.hideKeyboard(view, requireContext())
            addNewIngredient()
            findNavController().navigate(AddIngredientToShoppingListFragmentDirections.actionAddIngredientToShoppingListFragmentToShoppingListDetailFragment(viewModel.shoppingList.value!!))
        }
    }

    private fun addNewIngredient()
    {
        val amount = binding.editTextAmount.text.toString()
        val unit = binding.autoCompleteUnit.text.toString()
        val ingredientName = binding.autoCompleteIngredient.text.toString()

        val ingredient = viewModel.ingredientRepository.getIngredientForName(ingredientName)
        val shoppingListItem = ShoppingListItem(amount = amount, unit = unit)
        shoppingListItem.ingredient.target = ingredient

        viewModel.saveNewItem(shoppingListItem)
    }

    override fun onResume()
    {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Zutaten hinzufügen"
    }
}