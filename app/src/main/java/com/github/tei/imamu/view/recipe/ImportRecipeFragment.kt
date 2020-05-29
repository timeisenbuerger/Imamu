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
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentImportRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.ImportRecipeViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class ImportRecipeFragment : Fragment()
{
    private lateinit var binding: FragmentImportRecipeBinding
    private val viewModel: ImportRecipeViewModel by inject()
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezept importieren"

        init(inflater, container)
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
            importRecipe()
        }
    }

    private fun importRecipe()
    {
        if (!TextUtils.isEmpty(binding.editTextRecipeLink.text.toString()))
        {
            if (isNetworkConnected())
            {
                requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.editTextRecipeLink.visibility = View.GONE
                binding.buttonImportRecipe.visibility = View.GONE
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.shimmerViewContainer.startShimmerAnimation()

                doAsync {
                    viewModel.startImport(binding.editTextRecipeLink.text.toString())

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

    private fun isNetworkConnected(): Boolean
    {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
