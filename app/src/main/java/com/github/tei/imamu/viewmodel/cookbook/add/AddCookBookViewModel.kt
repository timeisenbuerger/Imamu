package com.github.tei.imamu.viewmodel.cookbook.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.entity.cookbook.CookBook
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Job

class AddCookBookViewModel(application: Application) : AndroidViewModel(application)
{
    private var viewModelJob = Job()

    private val _cookBook = MutableLiveData<CookBook>()
    val cookBooks: LiveData<CookBook>
        get() = _cookBook

    private val cookBookBox: Box<CookBook> = ObjectBox.boxStore.boxFor()

    init
    {
        _cookBook.value = CookBook()
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }
}