package com.github.tei.imamu.viewmodel.recipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddRecipeViewModel : ViewModel()
{
    var title = MutableLiveData<String>()
    var servingsNumber = MutableLiveData<Int>()
    var ingredients = MutableLiveData<String>()
    var preparation = MutableLiveData<String>()

}