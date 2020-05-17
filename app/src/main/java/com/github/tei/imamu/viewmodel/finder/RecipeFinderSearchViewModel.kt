package com.github.tei.imamu.viewmodel.finder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.Ingredient
import com.github.tei.imamu.data.repository.IngredientRepository
import io.objectbox.android.ObjectBoxLiveData

class RecipeFinderSearchViewModel(private val ingredientRepository: IngredientRepository) : ViewModel()
{
    private var _ingredients: ObjectBoxLiveData<Ingredient> = ingredientRepository.getAll()
    val ingredients: ObjectBoxLiveData<Ingredient>
        get() = _ingredients

    private val _navigateToSearchResult = MutableLiveData<Boolean>()
    val navigateToSearchResult: LiveData<Boolean>
        get() = _navigateToSearchResult

    fun onNavigateToSearchResult()
    {
        _navigateToSearchResult.value = true
    }

    fun onNavigateToSearchResultComplete()
    {
        _navigateToSearchResult.value = false
    }
}