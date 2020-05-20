package com.github.tei.imamu.wrapper

import com.github.tei.imamu.data.entity.recipe.Recipe
import java.io.Serializable

data class SingleSearchResultWrapper(var info: String?, var recipe: Recipe) : Serializable
{
}
