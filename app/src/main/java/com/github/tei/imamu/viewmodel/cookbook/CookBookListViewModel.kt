package com.github.tei.imamu.viewmodel.cookbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.entity.cookbook.CookBook
import com.github.tei.imamu.data.repository.CookBookRepository
import io.objectbox.android.ObjectBoxLiveData

class CookBookListViewModel(private val cookBookRepository: CookBookRepository) : ViewModel()
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
        for (cookBook in cookBooks)
        {
            cookBookRepository.remove(cookBook)
        }
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