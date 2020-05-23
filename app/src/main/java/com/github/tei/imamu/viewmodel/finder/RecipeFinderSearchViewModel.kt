package com.github.tei.imamu.viewmodel.finder

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.Recipe_
import com.github.tei.imamu.data.repository.IngredientRepository
import com.github.tei.imamu.data.repository.RecipeRepository
import com.github.tei.imamu.wrapper.FullSearchResultWrapper
import com.github.tei.imamu.wrapper.SingleSearchResultWrapper
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.query.QueryBuilder

class RecipeFinderSearchViewModel(private val recipeRepository: RecipeRepository, private val ingredientRepository: IngredientRepository) : ViewModel()
{
    private var _ingredients: ObjectBoxLiveData<Ingredient> = ingredientRepository.getAll()
    val ingredients: ObjectBoxLiveData<Ingredient>
        get() = _ingredients

    private val _navigateToSearchResult = MutableLiveData<FullSearchResultWrapper>()
    val navigateToSearchResult: LiveData<FullSearchResultWrapper>
        get() = _navigateToSearchResult

    private fun onNavigateToSearchResult(value: FullSearchResultWrapper)
    {
        _navigateToSearchResult.value = value
    }

    fun onNavigateToSearchResultComplete()
    {
        _navigateToSearchResult.value = null
    }

    fun startSearch(selectedChipValues: MutableMap<String, String>, selectedIngredients: MutableList<String>)
    {
        var fullSearchResultWrapper = FullSearchResultWrapper(mutableListOf())

        val query = buildQuery(selectedChipValues)
        val recipes = query.build()
            .findLazyCached()

        if (selectedIngredients.isNotEmpty())
        {
            for (recipe in recipes)
            {
                var isResult = false
                var containedIngredientNumber = 0

                for (recipeIngredient in recipe.recipeIngredients)
                {
                    if (selectedIngredients.contains(recipeIngredient.ingredient.target.name))
                    {
                        isResult = true
                        containedIngredientNumber++
                    }
                }

                if (isResult)
                {
                    var info = ""
                    val diff = recipe.recipeIngredients.size - containedIngredientNumber
                    info = if (diff == 0)
                    {
                        "Alle Zutaten vorhanden"
                    }
                    else
                    {
                        "$diff fehlende Zutaten"
                    }
                    val singleSearchResultWrapper = SingleSearchResultWrapper(info, recipe)
                    fullSearchResultWrapper.resultList.add(singleSearchResultWrapper)
                }
            }
        }
        else
        {
            for (recipe in recipes)
            {
                var singleSearchResultWrapper = SingleSearchResultWrapper(null, recipe)
                fullSearchResultWrapper.resultList.add(singleSearchResultWrapper)
            }
        }

        onNavigateToSearchResult(fullSearchResultWrapper)
    }

    private fun buildQuery(selectedChipValues: MutableMap<String, String>): QueryBuilder<Recipe>
    {
        var query = recipeRepository.recipeBox.query()
        var isAndNecessary = false
        if (!TextUtils.isEmpty(selectedChipValues["difficulty"]))
        {
            query = query.contains(Recipe_.difficulty, selectedChipValues["difficulty"])
            isAndNecessary = true
        }
        if (!TextUtils.isEmpty(selectedChipValues["type"]))
        {
            query = if (isAndNecessary)
            {
                query.and()
                    .contains(Recipe_.type, selectedChipValues["type"])
            }
            else
            {
                query.contains(Recipe_.type, selectedChipValues["type"])
            }
            isAndNecessary = true
        }
        if (!TextUtils.isEmpty(selectedChipValues["nutrition"]))
        {
            query = if (isAndNecessary)
            {
                query.and()
                    .contains(Recipe_.nutrition, selectedChipValues["nutrition"])
            }
            else
            {
                query.contains(Recipe_.nutrition, selectedChipValues["nutrition"])
            }
            isAndNecessary = true
        }
        if (!TextUtils.isEmpty(selectedChipValues["time"]))
        {
            var time: Long = -1
            when (selectedChipValues["time"])
            {
                "≤ 20 min" -> time = 21
                "≤ 30 min" -> time = 31
                "≤ 50 min" -> time = 51
                "≥ 60 min" -> time = 59
            }

            if (time < 60)
            {
                query = if (isAndNecessary)
                {
                    query.and()
                        .less(Recipe_.totalTime, time)
                }
                else
                {
                    query.less(Recipe_.totalTime, time)
                }
            }
            else
            {
                query = if (isAndNecessary)
                {
                    query.and()
                        .greater(Recipe_.totalTime, time)
                }
                else
                {
                    query.greater(Recipe_.totalTime, time)
                }
            }
        }
        return query
    }
}