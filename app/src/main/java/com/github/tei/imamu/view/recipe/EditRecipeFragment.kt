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
import com.github.tei.imamu.data.entity.Recipe
import com.github.tei.imamu.data.entity.RecipeIngredient
import com.github.tei.imamu.databinding.FragmentEditRecipeBinding
import com.github.tei.imamu.viewmodel.recipe.edit.EditRecipeViewModel
import com.github.tei.imamu.viewmodel.recipe.edit.EditRecipeViewModelFactory
import java.io.File

class EditRecipeFragment : Fragment()
{
    private val GALLERY_REQUEST_CODE = 28

    private lateinit var binding: FragmentEditRecipeBinding
    private lateinit var viewModel: EditRecipeViewModel
    private lateinit var viewModelFactory: EditRecipeViewModelFactory
    private lateinit var application: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Rezept bearbeiten"

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_recipe, container, false)

        //set recipe in binding
        binding.recipe = EditRecipeFragmentArgs.fromBundle(requireArguments()).recipe

        //init application
        application = requireNotNull(this.activity).application

        //init viewModel
        viewModelFactory = EditRecipeViewModelFactory(EditRecipeFragmentArgs.fromBundle(requireArguments()).recipe, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditRecipeViewModel::class.java)

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        val recipe = binding.recipe

        recipe?.let {
            if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
            {
                binding.imageViewMeal.setImageURI(Uri.parse(recipe.imagePath))
            }
            else
            {
                binding.imageViewMeal.setImageResource(R.drawable.ic_hot_tub)
            }

            when (recipe.difficulty)
            {
                "Einfach" -> binding.chipEasy.isChecked = true
                "Normal"  -> binding.chipNormal.isChecked = true
                "Schwer"  -> binding.chipHard.isChecked = true
            }

            initIngredientLines(recipe, inflater)
        }
    }

    private fun initIngredientLines(recipe: Recipe, inflater: LayoutInflater)
    {
        recipe.let {
            for (ingredient in recipe.recipeIngredients)
            {
                val ingredientLine = addIngredientRow(inflater)

                val editTextAmount = ingredientLine.findViewById<EditText>(R.id.edit_text_ingredient_amount)
                editTextAmount.setText(ingredient.amount, TextView.BufferType.EDITABLE)

                val autoCompleteUnit = ingredientLine.findViewById<AutoCompleteTextView>(R.id.auto_complete_ingredient_unit)
                autoCompleteUnit.setText(ingredient.unit, TextView.BufferType.EDITABLE)

                val editTextName = ingredientLine.findViewById<EditText>(R.id.edit_text_ingredient)
                editTextName.setText(ingredient.name, TextView.BufferType.EDITABLE)
            }
        }
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
                val recipe = binding.recipe
                recipe?.let {
                    findNavController().navigate(EditRecipeFragmentDirections.actionEditRecipeFragmentToRecipeDetailFragment(recipe))
                    viewModel.onNavigateToDetailComplete()
                }
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

    private fun addIngredientRow(inflater: LayoutInflater) : LinearLayout
    {
        val ingredientLine = inflater.inflate(R.layout.item_add_ingredient, binding.layoutContainerIngredients)

        val autoCompleteViewUnit = ingredientLine.findViewById<AutoCompleteTextView>(R.id.auto_complete_ingredient_unit)

        // Get the string array
        val units: Array<out String> = resources.getStringArray(R.array.ingredient_units)
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, units).also { adapter -> autoCompleteViewUnit.setAdapter(adapter) }

        val buttonClear = ingredientLine.findViewById<ImageButton>(R.id.image_button_remove_line)
        buttonClear.setOnClickListener { removeIngredientLine(it) }

        return ingredientLine as LinearLayout
    }

    private fun removeIngredientLine(button: View?)
    {
        val index = binding.layoutContainerIngredients.indexOfChild(button?.parent as View)
        binding.layoutContainerIngredients.removeViewAt(index)
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

                if (!TextUtils.isEmpty(editTextAmount.text) && !TextUtils.isEmpty(autoCompleteUnit.text) && !TextUtils.isEmpty(editTextIngredient.text))
                {
                    val recipeIngredient = RecipeIngredient()
                    recipeIngredient.amount = editTextAmount.text.toString()
                    recipeIngredient.unit = autoCompleteUnit.text.toString()
                    recipeIngredient.name = editTextIngredient.text.toString()

                    viewModel.recipe.value?.recipeIngredients?.add(recipeIngredient)
                }
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
