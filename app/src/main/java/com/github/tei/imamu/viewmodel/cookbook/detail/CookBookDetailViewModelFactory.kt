package com.github.tei.imamu.viewmodel.cookbook.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.tei.imamu.data.entity.cookbook.CookBook

class CookBookDetailViewModelFactory(private val application: Application, private val cookBook: CookBook) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(CookBookDetailViewModel::class.java))
        {
            return CookBookDetailViewModel(application, cookBook) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}