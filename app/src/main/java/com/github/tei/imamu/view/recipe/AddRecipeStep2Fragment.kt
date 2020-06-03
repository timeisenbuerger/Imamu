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
import com.github.tei.imamu.databinding.FragmentAddRecipeStep2Binding
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject

class AddRecipeStep2Fragment : Fragment()
{
    private lateinit var binding: FragmentAddRecipeStep2Binding
    private val viewModel: AddRecipeViewModel by inject()
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents(inflater)
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_step2, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        viewModel.recipe.value = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipe
    }

    private fun initListener()
    {
        binding.buttonContinue.setOnClickListener {
            updateRecipeDataFromUI()
            viewModel.onNavigateToNextStep()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToNextStep.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AddRecipeStep2FragmentDirections.actionAddRecipeStep2FragmentToAddRecipeStep3Fragment(it))
                viewModel.onNavigateToNextStepComplete()
            }
        })
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        //Recipe Difficulty
        createChip("Einfach", inflater, binding.chipGroupDifficulty)
        createChip("Mittel", inflater, binding.chipGroupDifficulty)
        createChip("Schwer", inflater, binding.chipGroupDifficulty)
    }

    private fun createChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_ingredient, parent, false) as Chip
        chip.text = name
        parent.addView(chip)

        if (name == viewModel.recipe.value!!.difficulty)
        {
            chip.isChecked = true
        }
    }

    private fun updateRecipeDataFromUI()
    {
        for(i in 0 until binding.chipGroupDifficulty.childCount)
        {
            val chip = binding.chipGroupDifficulty.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                viewModel.recipe.value!!.difficulty = chip.text.toString()
                break
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_close, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_close ->
            {
                findNavController().navigate(AddRecipeStep2FragmentDirections.actionAddRecipeStep2FragmentToNavRecipeList())
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
