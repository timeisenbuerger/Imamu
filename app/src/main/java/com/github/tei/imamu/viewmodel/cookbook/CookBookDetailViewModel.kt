package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.cookbook.LastViewedCookBook
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.CookBookRepository
import com.github.tei.imamu.data.repository.LastViewedCookBookRepository

class CookBookDetailViewModel(private val cookBookRepository: CookBookRepository, private val lastViewedCookBookRepository: LastViewedCookBookRepository) : ViewModel()
{
    val cookBook = MutableLiveData<CookBook>()

    private val _clickedRecipe = MutableLiveData<Recipe>()
    val clickedRecipe: LiveData<Recipe>
        get() = _clickedRecipe

    private val _navigateToRecipeDetail = MutableLiveData<Boolean>()
    val navigateToRecipeDetail: LiveData<Boolean>
        get() = _navigateToRecipeDetail

    fun onClickRecipe(item: Recipe)
    {
        _clickedRecipe.value = item
        _navigateToRecipeDetail.value = true
    }

    fun onNavigateToRecipeDetailComplete()
    {
        _navigateToRecipeDetail.value = false
    }

    fun updateLastViewed()
    {
        val lastViewed = lastViewedCookBookRepository.getAllAsLazyList()
        lastViewed.let {
            val containsCookBook = lastViewedContainsCookBook(it)
            if (it.size == 10 && !containsCookBook)
            {
                //remove oldest entry
                lastViewedCookBookRepository.remove(it.first())

                addNewLastViewedEntry()
            }
            else if (!containsCookBook)
            {
                addNewLastViewedEntry()
            }
        }
    }

    private fun addNewLastViewedEntry()
    {
        //add new entry
        val newLastViewedCookBook = LastViewedCookBook()
        newLastViewedCookBook.cookBook.target = cookBook.value
        lastViewedCookBookRepository.save(newLastViewedCookBook)
    }

    private fun lastViewedContainsCookBook(list: List<LastViewedCookBook>) : Boolean
    {
        var containsCookBook = false
        for (lastViewedCookBook in list)
        {
            if (lastViewedCookBook.cookBook.target == cookBook.value)
            {
                containsCookBook = true
                break
            }
        }

        return containsCookBook
    }
}