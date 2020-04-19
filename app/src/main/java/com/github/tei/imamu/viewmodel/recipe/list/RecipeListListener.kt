package com.github.tei.imamu.viewmodel.recipe.list

import com.github.tei.imamu.data.entity.Recipe

class RecipeListListener(val clickListener: (recipe: Recipe) -> Unit)
{
    fun onClick(recipe: Recipe) = clickListener(recipe)
}