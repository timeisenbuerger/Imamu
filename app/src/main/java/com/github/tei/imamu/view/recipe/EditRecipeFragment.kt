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
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.recipe.IngredientAddEditAdapter
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.databinding.FragmentEditRecipeBinding
import com.github.tei.imamu.util.setListViewHeightBasedOnChildren
import com.github.tei.imamu.viewmodel.recipe.EditRecipeViewModel
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import java.io.File

class EditRecipeFragment : Fragment()
{
    private val GALLERY_REQUEST_CODE = 28

    private lateinit var binding: FragmentEditRecipeBinding
    private val viewModel: EditRecipeViewModel by inject()
    private lateinit var application: Application
    private lateinit var adapter: IngredientAddEditAdapter

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
        viewModel.recipe.value = EditRecipeFragmentArgs.fromBundle(requireArguments()).recipe

        //set lifecycle owner
        binding.lifecycleOwner = viewLifecycleOwner

        //set viewModel in binding
        binding.viewModel = viewModel
    }

    private fun initComponents(inflater: LayoutInflater)
    {
        val recipe = viewModel.recipe.value

        recipe?.let {
            if (!TextUtils.isEmpty(recipe.imagePath) && File(recipe.imagePath).exists())
            {
                binding.imageViewMeal.setImageURI(Uri.parse(recipe.imagePath))
            }
            else
            {
                binding.imageViewMeal.setImageResource(R.drawable.ic_hot_tub)
            }

            adapter = IngredientAddEditAdapter(requireContext(), viewModel.recipe.value!!.recipeIngredients)
            binding.listViewIngredients.adapter = adapter
            setListViewHeightBasedOnChildren(binding.listViewIngredients)

            initChips(it, inflater)
        }
    }

    private fun initListener()
    {
        binding.imageViewMeal.setOnClickListener {
            pickImageFromGallery()
        }

        binding.buttonAddIngredient.setOnClickListener {
            adapter.add(RecipeIngredient())
            adapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(binding.listViewIngredients)
        }
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

    private fun initChips(recipe: Recipe, inflater: LayoutInflater)
    {
        //Type of Recipe
        createChip("Vorspeise", inflater, binding.chipGroupRecipeType)
        createChip("Salat", inflater, binding.chipGroupRecipeType)
        createChip("Hauptspeise", inflater, binding.chipGroupRecipeType)
        createChip("Dessert", inflater, binding.chipGroupRecipeType)
        createChip("Getränk", inflater, binding.chipGroupRecipeType)

        //Recipe Feature
        createChip("Vegetarisch", inflater, binding.chipGroupNutrition)
        createChip("Vegan", inflater, binding.chipGroupNutrition)
        createChip("Laktosefrei", inflater, binding.chipGroupNutrition)
        createChip("Glutenfrei", inflater, binding.chipGroupNutrition)
        createChip("Kalorienarm", inflater, binding.chipGroupNutrition)

        //Recipe Difficulty
        createChip("Einfach", inflater, binding.chipGroupDifficulty)
        createChip("Mittel", inflater, binding.chipGroupDifficulty)
        createChip("Schwer", inflater, binding.chipGroupDifficulty)

        for(i in 0 until binding.chipGroupRecipeType.childCount)
        {
            val chip = binding.chipGroupRecipeType.getChildAt(i) as Chip
            if (chip.text.toString() == recipe.type)
            {
                chip.isChecked = true
                break
            }
        }

        val nutritionOptions = recipe.nutrition.split(";").toMutableList()
        for(i in 0 until binding.chipGroupNutrition.childCount)
        {
            val chip = binding.chipGroupNutrition.getChildAt(i) as Chip
            if (nutritionOptions.contains(chip.text.toString()))
            {
                chip.isChecked = true
            }
        }

        for(i in 0 until binding.chipGroupDifficulty.childCount)
        {
            val chip = binding.chipGroupDifficulty.getChildAt(i) as Chip
            if (chip.text.toString() == recipe.difficulty)
            {
                chip.isChecked = true
                break
            }
        }
    }

    private fun createChip(name: String, inflater: LayoutInflater, parent: ViewGroup)
    {
        val chip = inflater.inflate(R.layout.item_chip_ingredient, parent, false) as Chip
        chip.text = name
        parent.addView(chip)
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
                updateRecipeDataFromUI()
                viewModel.onSaveRecipe()
            }
            true
        }
        else                     ->
        {
            Toast.makeText(context, "Back", Toast.LENGTH_SHORT)
                .show()
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

    private fun updateRecipeDataFromUI()
    {
        for(i in 0 until binding.chipGroupRecipeType.childCount)
        {
            val chip = binding.chipGroupRecipeType.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                viewModel.recipe.value!!.type = chip.text.toString()
                break
            }
        }

        for(i in 0 until binding.chipGroupDifficulty.childCount)
        {
            val chip = binding.chipGroupDifficulty.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                viewModel.recipe.value!!.difficulty = chip.text.toString()
                break
            }
        }

        for(i in 0 until binding.chipGroupNutrition.childCount)
        {
            val chip = binding.chipGroupNutrition.getChildAt(i) as Chip
            if (chip.isChecked)
            {
                viewModel.recipe.value!!.nutrition += chip.text.toString() + ";"
            }
        }
    }
}
