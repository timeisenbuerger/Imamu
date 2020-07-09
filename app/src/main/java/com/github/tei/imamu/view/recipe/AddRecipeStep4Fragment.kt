package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentAddRecipeStep4Binding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import org.koin.android.ext.android.inject

class AddRecipeStep4Fragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeStep4Binding
    private val viewModel: AddRecipeViewModel by inject()
    private lateinit var application: Application
    private var isEdit = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_step4, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        viewModel.recipe.value = AddRecipeStep4FragmentArgs.fromBundle(requireArguments()).recipe
        isEdit = AddRecipeStep4FragmentArgs.fromBundle(requireArguments()).isEdit
    }

    private fun initListener()
    {
        binding.buttonContinue.setOnClickListener {
            viewModel.onNavigateToNextStep()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToNextStep.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AddRecipeStep4FragmentDirections.actionAddRecipeStep4FragmentToAddRecipeStep5Fragment(it))
                viewModel.onNavigateToNextStepComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        if (isEdit)
        {
            inflater.inflate(R.menu.menu_save_close, menu)
        }
        else
        {
            inflater.inflate(R.menu.menu_close, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_close ->
            {
                findNavController().navigate(AddRecipeStep4FragmentDirections.actionAddRecipeStep4FragmentToNavRecipeList())
            }
            R.id.action_save  ->
            {
                viewModel.onSaveRecipe()
                findNavController().navigate(AddRecipeStep4FragmentDirections.actionAddRecipeStep4FragmentToRecipeDetailFragment(viewModel.recipe.value!!))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume()
    {
        super.onResume()
        if (viewModel.recipe.value!!.id != 0L)
        {
            (activity as MainActivity).supportActionBar?.title = "Rezept bearbeiten"
        }
        else
        {
            (activity as MainActivity).supportActionBar?.title = "Rezept hinzufügen"
        }
    }
}
