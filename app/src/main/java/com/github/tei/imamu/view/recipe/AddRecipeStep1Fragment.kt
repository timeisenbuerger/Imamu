package com.github.tei.imamu.view.recipe

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.databinding.FragmentAddRecipeStep1Binding
import com.github.tei.imamu.util.RealPathUtil
import com.github.tei.imamu.viewmodel.recipe.AddRecipeViewModel
import org.koin.android.ext.android.inject

class AddRecipeStep1Fragment : Fragment()
{
    companion object
    {
        private const val GALLERY_REQUEST_CODE = 28
    }

    private lateinit var binding: FragmentAddRecipeStep1Binding
    private val viewModel: AddRecipeViewModel by inject()
    private lateinit var application: Application
    private var isEdit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_step1, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        viewModel.recipe.value = AddRecipeStep1FragmentArgs.fromBundle(requireArguments()).recipe
        isEdit = AddRecipeStep1FragmentArgs.fromBundle(requireArguments()).isEdit
    }

    private fun initListener()
    {
        binding.imageViewMeal.setOnClickListener {
            pickImageFromGallery()
        }

        binding.buttonContinue.setOnClickListener {
            viewModel.onNavigateToNextStep()
        }
    }

    private fun initObserver()
    {
        viewModel.navigateToNextStep.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(AddRecipeStep1FragmentDirections.actionAddRecipeStep1FragmentToAddRecipeStep2Fragment(it, isEdit))
                viewModel.onNavigateToNextStepComplete()
            }
        })
    }

    private fun pickImageFromGallery()
    {
        //Create an Intent with action as ACTION_PICK
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        // Sets the type as image/*. This ensures only components of type image are selected
        gallery.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        gallery.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(gallery, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) when (requestCode)
        {
            GALLERY_REQUEST_CODE ->
            {
                data?.let {
                    val uri = it.data
                    val selectedImage: Uri = uri!!
                    binding.imageViewMeal.setImageURI(selectedImage)

                    binding.imageViewMeal.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.viewModel!!.recipe.value!!.imagePath = RealPathUtil.getRealPath(requireContext(), uri)!!
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        if (isEdit)
        {
            inflater.inflate(R.menu.menu_save_close, menu)
        }
        else
        {
            inflater.inflate(R.menu.menu_close, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_close ->
            {
                findNavController().navigate(AddRecipeStep1FragmentDirections.actionAddRecipeStep1FragmentToNavRecipeList())
            }
            R.id.action_save ->
            {
                viewModel.onSaveRecipe()
                findNavController().navigate(AddRecipeStep1FragmentDirections.actionAddRecipeStep1FragmentToRecipeDetailFragment(viewModel.recipe.value!!))
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
