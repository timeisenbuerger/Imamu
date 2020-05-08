package com.github.tei.imamu.viewmodel.cookbook.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.cookbook.CookBook
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class CookBookListViewModel(application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private val _cookBooks = MutableLiveData<List<CookBook>>()
    val cookBooks: LiveData<List<CookBook>>
        get() = _cookBooks

    private val _navigateToDetail = MutableLiveData<CookBook>()
    val navigateToDetail: LiveData<CookBook>
        get() = _navigateToDetail

    private val cookBookBox: Box<CookBook> = ObjectBox.boxStore.boxFor()

    init
    {
        initCookbooks()
    }

    fun initCookbooks()
    {
        _cookBooks.value = cookBookBox.query()
            .build()
            .findLazyCached()
    }

    fun deleteRecipes(cookBooks: List<CookBook>)
    {
        cookBookBox.remove(cookBooks)
    }

    fun onCookBookClicked(cookBook: CookBook)
    {
        _navigateToDetail.value = cookBook
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}