package com.github.tei.imamu.viewmodel.finder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.repository.RecipeRepository
import com.github.tei.imamu.wrapper.FullSearchResultWrapper
import com.github.tei.imamu.wrapper.SingleSearchResultWrapper

class RecipeFinderResultListViewModel(private val recipeRepository: RecipeRepository) : ViewModel()
{
    private var _results = MutableLiveData<MutableList<SingleSearchResultWrapper>>()
    val results: LiveData<MutableList<SingleSearchResultWrapper>>
        get() = _results

    private var _recipes = MutableLiveData<MutableList<Recipe>>()
    val recipes: LiveData<MutableList<Recipe>>
        get() = _recipes

    private val _navigateToDetail = MutableLiveData<Recipe>()
    val navigateToDetail: LiveData<Recipe>
        get() = _navigateToDetail

    fun initRecipes(fullSearchResultWrapper: FullSearchResultWrapper)
    {
        _results.value = fullSearchResultWrapper.resultList
        val recipeList = mutableListOf<Recipe>()
        for (wrapper in fullSearchResultWrapper.resultList)
        {
            recipeList.add(wrapper.recipe)
        }
        _recipes.value = recipeList
    }

    fun getWrapper(recipe: Recipe): SingleSearchResultWrapper?
    {
        var value: SingleSearchResultWrapper? = null
        results.value?.let {
            for (wrapper in it)
            {
                if (wrapper.recipe.equals(recipe))
                {
                    value = wrapper
                }
            }
        }
        return value
    }

    fun onRecipeClicked(recipe: Recipe)
    {
        _navigateToDetail.value = recipe
    }

    fun onNavigateToDetailComplete()
    {
        _navigateToDetail.value = null
    }
}