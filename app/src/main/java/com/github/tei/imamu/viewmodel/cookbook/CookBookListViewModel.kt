package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.cookbook.LastViewedCookBook
import com.github.tei.imamu.data.repository.CookBookRepository
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.LastViewedCookBookRepository
import io.objectbox.android.ObjectBoxLiveData

class CookBookListViewModel(private val cookBookRepository: CookBookRepository, private val lastViewedCookBookRepository: LastViewedCookBookRepository, val ingredientRepository: IngredientRepository) : ViewModel()
{
    private var _cookBooks: ObjectBoxLiveData<CookBook> = cookBookRepository.getAll()
    val cookBooks: ObjectBoxLiveData<CookBook>
        get() = _cookBooks

    private val _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private val _navigateToDetail = MutableLiveData<CookBook>()
    val navigateToDetail: LiveData<CookBook>
        get() = _navigateToDetail

    fun initCookBooks()
    {
        _cookBooks = cookBookRepository.getAll()
    }

    fun deleteCookBooks(cookBooks: List<CookBook>)
    {
        val lastViewedCookBooks = lastViewedCookBookRepository.getAllAsLazyList()

        for (cookBook in cookBooks)
        {
            var lastViewedCookBookToRemove: LastViewedCookBook? = null
            for (lastViewedCookBook in lastViewedCookBooks)
            {
                if (lastViewedCookBook.cookBook.target == cookBook)
                {
                    lastViewedCookBookToRemove = lastViewedCookBook
                    break
                }
            }

            lastViewedCookBookToRemove?.let {
                lastViewedCookBookRepository.remove(it)
            }

            cookBookRepository.remove(cookBook)
        }
    }

    fun saveNewCookBook(newCookBook: CookBook)
    {
        cookBookRepository.save(newCookBook)
    }

    fun onCookBookClicked(cookBook: CookBook)
    {
        _navigateToDetail.value = cookBook
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }

    fun onNavigateToAdd()
    {
        _navigateToAdd.value = true
    }

    fun onNavigateToAddComplete()
    {
        _navigateToAdd.value = false
    }
}