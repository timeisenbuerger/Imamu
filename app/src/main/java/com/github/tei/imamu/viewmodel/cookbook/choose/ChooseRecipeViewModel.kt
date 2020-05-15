package com.github.tei.imamu.viewmodel.cookbook.choose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.data.entity.recipe.Recipe
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class ChooseRecipeViewModel(application: Application, cookBook: CookBook) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private val _recipes = MutableLiveData<MutableList<Recipe>>()
    val recipes: LiveData<MutableList<Recipe>>
        get() = _recipes

    private val _cookBook = MutableLiveData<CookBook>()
    val cookBook: LiveData<CookBook>
        get() = _cookBook

    private val recipeBox: Box<Recipe> = ObjectBox.boxStore.boxFor()

    init
    {
        _recipes.value = recipeBox.query()
            .build()
            .findLazyCached()

        _cookBook.value = cookBook
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}