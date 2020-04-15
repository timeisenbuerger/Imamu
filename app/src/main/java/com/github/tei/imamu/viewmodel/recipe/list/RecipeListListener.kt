package com.github.tei.imamu.viewmodel.recipe.list

import com.github.tei.imamu.data.entity.Recipe

class RecipeListListener(val clickListener: (id: Long) -> Unit)
{
    fun onClick(recipe: Recipe) = clickListener(recipe.id)
}