package com.github.tei.imamu.view.recipe

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.R
import com.github.tei.imamu.data.ImamuDatabase
import com.github.tei.imamu.data.dao.RecipeDao
import com.github.tei.imamu.databinding.FragmentAddRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.add.AddRecipeViewModel
import com.github.tei.imamu.viewmodel.recipe.add.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment()
{
    private val GALLERY_REQUEST_CODE = 28

    private lateinit var binding: FragmentAddRecipeBinding
    private lateinit var viewModel: AddRecipeViewModel
    private lateinit var viewModelFactory: AddRecipeViewModelFactory
    private lateinit var application: Application
    private lateinit var recipeDao: RecipeDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe, container, false)

        //init dao
        application = requireNotNull(this.activity).application
        recipeDao = ImamuDatabase.getInstance(application).recipeDao

        //init viewModel
        viewModelFactory = AddRecipeViewModelFactory(recipeDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddRecipeViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initComponents()
    {
        binding.imageViewMeal.setImageResource(R.drawable.ic_hot_tub)
    }

    private fun initListener()
    {
        binding.chipGroupDifficulty.setOnCheckedChangeListener { _, checkedId: Int ->
            when (checkedId)
            {
                R.id.chip_easy   -> viewModel.recipe.value!!.difficulty = "Einfach"
                R.id.chip_normal -> viewModel.recipe.value!!.difficulty = "Normal"
                R.id.chip_hard   -> viewModel.recipe.value!!.difficulty = "Schwer"
            }
        }

        binding.imageViewMeal.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer { saveRecipe ->
            if (saveRecipe)
            {
                findNavController().navigate(AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeDetailFragment(viewModel.recipe.value!!))
                viewModel.onNavigateToDetailComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)
    {
        R.id.action_save_changes ->
        {
            if (isSavePossible())
            {
                viewModel.onSaveRecipe()
            }
            true
        }
        else                     ->
        {
            Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }
    }

    private fun isSavePossible(): Boolean
    {
        var result = true

        when
        {
            TextUtils.isEmpty(binding.editTextRecipeTitle.text)            ->
            {
                binding.editTextRecipeTitle.error = "Bitte gebe einen Rezeptenamen an."
                result = false
            }
            TextUtils.isEmpty(binding.editTextNumberServings.text)         ->
            {
                binding.editTextNumberServings.error = "Bitte gebe eine Anzahl von Portionen an."
                result = false
            }
            TextUtils.isEmpty(binding.multiEditTextRecipeIngredients.text) ->
            {
                binding.multiEditTextRecipeIngredients.error = "Bitte gebe Zutaten an."
                result = false
            }
            TextUtils.isEmpty(binding.multiEditTextRecipePreparation.text) ->
            {
                binding.multiEditTextRecipePreparation.error = "Bitte gebe eine Zubereitungsbeschreibung ein."
                result = false
            }
        }

        return result
    }

    private fun pickImageFromGallery()
    {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) when (requestCode)
        {
            GALLERY_REQUEST_CODE ->
            {
                data?.let { uri ->
                    val selectedImage: Uri = uri.data!!
                    binding.imageViewMeal.setImageURI(selectedImage)

                    binding.viewModel!!.recipe.value!!.imagePath = getRealPathFromURI(uri.data)!!
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String?
    {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}
