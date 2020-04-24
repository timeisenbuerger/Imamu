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
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.data.entity.RecipeIngredient
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezept hinzufügen"

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

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = AddRecipeViewModelFactory(application)
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

        binding.buttonAddIngredient.setOnClickListener { addIngredientRow(layoutInflater) }
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

    private fun getRealPathFromURI(contentUri: Uri?): String?
    {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
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

    private fun addIngredientRow(inflater: LayoutInflater)
    {
        val ingredientLine = inflater.inflate(R.layout.item_add_ingredient, binding.layoutContainerIngredients)

        val autoCompleteViewUnit = ingredientLine.findViewById<AutoCompleteTextView>(R.id.auto_complete_ingredient_unit)

        // Get the string array
        val units: Array<out String> = resources.getStringArray(R.array.ingredient_units)
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, units).also { adapter -> autoCompleteViewUnit.setAdapter(adapter) }
    }

    private fun updateViewModelWithIngredients()
    {
        for (i in 0..binding.layoutContainerIngredients.childCount step 1)
        {
            binding.layoutContainerIngredients.getChildAt(i)?.let {
                val child = it as LinearLayout

                val editTextAmount = child.getChildAt(0) as EditText
                val autoCompleteUnit = child.getChildAt(1) as AutoCompleteTextView
                val editTextIngredient = child.getChildAt(2) as EditText

                val recipeIngredient = RecipeIngredient()
                recipeIngredient.amount = editTextAmount.text.toString()
                recipeIngredient.unit = autoCompleteUnit.text.toString()
                recipeIngredient.name = editTextIngredient.text.toString()

                viewModel.recipe.value?.let { value -> value.recipeIngredients.add(recipeIngredient) }
            }
        }
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
                updateViewModelWithIngredients()
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
            TextUtils.isEmpty(binding.multiEditTextRecipePreparation.text) ->
            {
                binding.multiEditTextRecipePreparation.error = "Bitte gebe eine Zubereitungsbeschreibung ein."
                result = false
            }
        }

        return result
    }
}
