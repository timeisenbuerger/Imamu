package com.github.tei.imamu.custom.listener

import com.github.tei.imamu.data.database.entity.recipe.Recipe

class RecipeListListener(val clickListener: (recipe: Recipe) -> Unit)
{
    fun onClick(recipe: Recipe) = clickListener(recipe)
}