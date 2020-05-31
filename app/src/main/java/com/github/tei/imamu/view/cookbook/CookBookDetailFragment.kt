package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookDetailRecipeListAdapter
import com.github.tei.imamu.databinding.FragmentDetailCookBookBinding
import com.github.tei.imamu.util.ShareUtil
import com.github.tei.imamu.viewmodel.cookbook.CookBookDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class CookBookDetailFragment : Fragment()
{
    private lateinit var binding: FragmentDetailCookBookBinding
    private val viewModel: CookBookDetailViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: CookBookDetailRecipeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateLastViewed()
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_cook_book, container, false)

        //init application
        application = requireNotNull(this.activity).application

        viewModel.cookBook.value = CookBookDetailFragmentArgs.fromBundle(requireArguments()).cookBook

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = CookBookDetailRecipeListAdapter(viewModel)
        binding.recipeList.adapter = listAdapter

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager
    }

    private fun initObserver()
    {
        viewModel.cookBook.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it.recipes)
            }
        })

        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(CookBookDetailFragmentDirections.actionCookBookDetailFragmentToRecipeDetailFragment(viewModel.clickedRecipe.value!!))
                viewModel.onNavigateToRecipeDetailComplete()
            }
        })
    }

    private fun shareCookBook()
    {
        val cookBook = viewModel.cookBook.value!!
        ShareUtil.shareCookBook(cookBook, requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action_edit_share, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_share -> shareCookBook()
            R.id.action_edit  -> findNavController().navigate(CookBookDetailFragmentDirections.actionCookBookDetailFragmentToEditCookBookFragment(viewModel.cookBook.value!!))
            else              -> findNavController().popBackStack()
        }
        return true
    }

    override fun onResume()
    {
        super.onResume()
        val mainActivity = (activity as MainActivity)
        mainActivity.setSupportActionBar(mainActivity.toolbar)
        mainActivity.supportActionBar?.title = viewModel.cookBook.value!!.title
    }
}