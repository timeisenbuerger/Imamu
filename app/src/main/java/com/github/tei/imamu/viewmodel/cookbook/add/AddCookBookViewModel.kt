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

    val cookBook = MutableLiveData<CookBook>()

    private val _navigateToChooseRecipe = MutableLiveData<Boolean>()
    val navigateToChooseRecipe: LiveData<Boolean>
        get() = _navigateToChooseRecipe

    private val _navigateToCookBookList = MutableLiveData<Boolean>()
    val navigateToCookBookList: LiveData<Boolean>
        get() = _navigateToCookBookList

    private val cookBookBox: Box<CookBook> = ObjectBox.boxStore.boxFor()

    init
    {
        cookBook.value = CookBook()
    }

    override fun onCleared()
    {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun saveCookBook()
    {
        cookBookBox.put(cookBook.value!!)
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