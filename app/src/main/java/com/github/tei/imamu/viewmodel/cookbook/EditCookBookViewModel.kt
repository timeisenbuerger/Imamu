package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.repository.CookBookRepository

class EditCookBookViewModel(private val cookBookRepository: CookBookRepository) : ViewModel()
{
    val cookBook = MutableLiveData<CookBook>()

    private val _navigateToChooseRecipe = MutableLiveData<Boolean>()
    val navigateToChooseRecipe: LiveData<Boolean>
        get() = _navigateToChooseRecipe

    private val _navigateToCookBookList = MutableLiveData<Boolean>()
    val navigateToCookBookList: LiveData<Boolean>
        get() = _navigateToCookBookList

    fun saveCookBook()
    {
        cookBookRepository.save(cookBook.value!!)
        _navigateToCookBookList.value = true
    }

    fun navigateToChooseRecipe()
    {
        _navigateToChooseRecipe.value = true
    }

    fun navigateToChooseRecipeComplete()
    {
        _navigateToChooseRecipe.value = false
    }

    fun navigateToCookBookListComplete()
    {
        _navigateToCookBookList.value = false
    }
}