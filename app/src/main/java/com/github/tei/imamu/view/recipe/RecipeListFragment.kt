package com.github.tei.imamu.view.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(RecipeListFragmentDirections.actionRecipeListFragmentToAddRecipeFragment())
        }

        return binding.root
    }
}
