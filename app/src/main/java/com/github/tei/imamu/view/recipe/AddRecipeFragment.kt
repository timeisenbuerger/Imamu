package com.github.tei.imamu.view.recipe

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentAddRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeBinding
    private lateinit var viewModel: AddRecipeViewModel
    private lateinit var viewModelFactory: AddRecipeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe, container, false)

        //placeholder
        binding.imageViewMeal.setImageResource(R.drawable.ic_camera)

        viewModelFactory = AddRecipeViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddRecipeViewModel::class.java)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_save_changes -> Toast.makeText(context, "Save", Toast.LENGTH_SHORT)
            else                     -> Toast.makeText(context, "Back", Toast.LENGTH_SHORT)
        }
        return super.onOptionsItemSelected(item)
    }
}
