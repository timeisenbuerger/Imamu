package com.github.tei.imamu.view.recipe

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.PreviewRecipeListAdapter
import com.github.tei.imamu.databinding.FragmentImportRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.ImportRecipeViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject
import kotlin.math.abs

class ImportRecipeFragment : Fragment()
{
    private lateinit var binding: FragmentImportRecipeBinding
    private val viewModel: ImportRecipeViewModel by inject()
    private lateinit var application: Application
    private lateinit var previewChefKochRecipeListAdapter: PreviewRecipeListAdapter
    private lateinit var previewKitchenStoriesRecipeListAdapter: PreviewRecipeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezept importieren"

        init(inflater, container)
        initComponents()
        initObserver()
        initListener()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_import_recipe, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = viewLifecycleOwner

        //set viewModel in binding
        binding.viewModel = viewModel

        previewChefKochRecipeListAdapter = PreviewRecipeListAdapter(viewModel, this)
        binding.chefkochRecipePreviewList.adapter = previewChefKochRecipeListAdapter

        previewKitchenStoriesRecipeListAdapter = PreviewRecipeListAdapter(viewModel, this)
        binding.kitchenstoriesRecipePreviewList.adapter = previewKitchenStoriesRecipeListAdapter
    }

    private fun initComponents()
    {
        initViewPager(binding.chefkochRecipePreviewList)
        initViewPager(binding.kitchenstoriesRecipePreviewList)

        fillViewPager()
    }

    private fun initObserver()
    {
        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer {
            val recipe = viewModel.recipe.value
            findNavController().navigate(ImportRecipeFragmentDirections.actionImportRecipeFragmentToRecipeDetailFragment(recipe!!))
        })
    }

    private fun initListener()
    {
        binding.buttonImportRecipe.setOnClickListener {
            importRecipe(binding.editTextRecipeLink.text.toString())
        }
    }

    private fun initViewPager(viewPager: ViewPager2)
    {
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.95f + r * 0.05f)
        }

        viewPager.setPageTransformer(compositePageTransformer)
    }

    private fun fillViewPager()
    {
        binding.progressBarChefkoch.visibility = View.VISIBLE
        binding.progressBarKitchenstories.visibility = View.VISIBLE

        doAsync {
            val recipesOfTheWeek = viewModel.retrieveChefKochRotW()
            uiThread {
                binding.progressBarChefkoch.visibility = View.GONE

                previewChefKochRecipeListAdapter.submitList(recipesOfTheWeek)
                binding.chefkochRecipePreviewList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
        }

        doAsync {
            val recipesOfTheWeek = viewModel.retrieveKitchenStoriesRotW()
            uiThread {
                binding.progressBarKitchenstories.visibility = View.GONE

                previewKitchenStoriesRecipeListAdapter.submitList(recipesOfTheWeek)
                binding.kitchenstoriesRecipePreviewList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
        }
    }

    fun importRecipe(url: String)
    {
        if (!TextUtils.isEmpty(url))
        {
            if (isNetworkConnected())
            {
                disableInteractions()

                doAsync {
                    viewModel.startImport(url)

                    uiThread {
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.shimmerViewContainer.stopShimmerAnimation()
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        viewModel.onNavigateToDetail()
                    }
                }
            }
            else
            {
                AlertDialog.Builder(requireContext())
                    .setTitle("Keine Verbindung zum Internet")
                    .setMessage("Bitte überprüfe deine Verbindung zum Internet und versuche es erneut")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .show()
            }
        }
        else
        {
            binding.editTextRecipeLink.error = "Bitte gebe einen Rezeptlink ein."
        }
    }

    private fun disableInteractions()
    {
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding.editTextRecipeLink.visibility = View.GONE
        binding.buttonImportRecipe.visibility = View.GONE
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmerAnimation()
    }

    private fun isNetworkConnected(): Boolean
    {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
