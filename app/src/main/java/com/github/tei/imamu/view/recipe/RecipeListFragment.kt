package com.github.tei.imamu.view.recipe

import android.app.Application
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.RecipeListAdapter
import com.github.tei.imamu.databinding.FragmentRecipeListBinding
import com.github.tei.imamu.viewmodel.recipe.RecipeListViewModel
import org.koin.android.ext.android.inject

class RecipeListFragment : Fragment()
{
    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel: RecipeListViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: RecipeListAdapter

    private lateinit var fabOpenAnim: Animation
    private lateinit var fabCloseAnim: Animation
    private var isOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezeptliste"

        init(inflater, container)
        initComponents()
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = RecipeListAdapter(viewModel)
        binding.recipeList.adapter = listAdapter

        val manager = LinearLayoutManager(activity)
        binding.recipeList.layoutManager = manager
    }

    private fun initComponents()
    {
        fabOpenAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabCloseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
    }

    private fun initListener()
    {
        binding.mainFab.setOnClickListener {
            if (isOpen)
            {
                binding.createRecipeFab.animation = fabCloseAnim
                binding.importRecipeFab.animation = fabCloseAnim

                binding.textViewCreate.visibility = View.INVISIBLE
                binding.textViewImport.visibility = View.INVISIBLE

                isOpen = false
            }
            else
            {
                binding.createRecipeFab.animation = fabOpenAnim
                binding.importRecipeFab.animation = fabOpenAnim

                binding.textViewCreate.visibility = View.VISIBLE
                binding.textViewImport.visibility = View.VISIBLE

                isOpen = true
            }
        }

        binding.createRecipeFab.setOnClickListener {
            findNavController().navigate(RecipeListFragmentDirections.actionRecipeListFragmentToAddRecipeFragment())
            isOpen = false
        }

        binding.importRecipeFab.setOnClickListener {
            findNavController().navigate(RecipeListFragmentDirections.actionNavRecipeListToImportRecipeFragment())
            isOpen = false
        }
    }

    private fun initObserver()
    {
        viewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.allRecipes = it
                listAdapter.submitList(it)
                binding.recipeList.scheduleLayoutAnimation()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                findNavController().navigate(RecipeListFragmentDirections.actionNavRecipeListToRecipeDetailFragment(recipe))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action_search_view, menu)
        val item = menu.findItem(R.id.action_search)
        item?.let {
            val searchView = it.actionView as SearchView
            searchView.isIconified = false
            val textListener = object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    listAdapter.filter.filter(newText)
                    return true
                }
            }
            searchView.setOnQueryTextListener(textListener)
        }
    }

    override fun onResume()
    {
        super.onResume()
        viewModel.initRecipes()
    }
}
