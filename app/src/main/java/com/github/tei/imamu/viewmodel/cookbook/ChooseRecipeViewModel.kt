package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.data.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.CookBookRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import io.objectbox.android.ObjectBoxLiveData

class ChooseRecipeViewModel(private val cookBookRepository: CookBookRepository, private val recipeRepository: RecipeRepository) : ViewModel()
{
    private var _recipes: ObjectBoxLiveData<Recipe> = recipeRepository.getAll()
    val recipes: ObjectBoxLiveData<Recipe>
        get() = _recipes

    val cookBook = MutableLiveData<CookBook>()

    init
    {
        _recipes = recipeRepository.getAll()
    }
}