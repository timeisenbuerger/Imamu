package com.github.tei.imamu.util

import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import org.json.JSONArray
import org.json.JSONObject

class JsonUtil
{
    companion object
    {
        fun toJson(cookBook: CookBook): JSONObject
        {
            val jSonObj = JSONObject()
            jSonObj.put("name", cookBook.title)

            val jsonArr = JSONArray()
            for (recipe in cookBook.recipes)
            {
                val recipeObj = JSONObject()

                recipeObj.put("title", recipe.title)
                recipeObj.put("servingsNumber", recipe.servingsNumber)

                val recipeIngredients = JSONArray()
                for (ingredient in recipe.recipeIngredients)
                {
                    val ingredientObj = JSONObject()
                    ingredientObj.put("amount", ingredient.amount)
                    ingredientObj.put("unit", ingredient.unit)
                    ingredientObj.put("name", ingredient.ingredient.target.name)
                    recipeIngredients.put(ingredientObj)
                }
                recipeObj.put("recipeIngredients", recipeIngredients)

                recipeObj.put("preparation", recipe.preparation)
                recipeObj.put("bakingTime", recipe.bakingTime)
                recipeObj.put("preparationTime", recipe.preparationTime)
                recipeObj.put("restTime", recipe.restTime)
                recipeObj.put("totalTime", recipe.totalTime)
                recipeObj.put("difficulty", recipe.difficulty)

                val recipeFeatures = JSONArray()
                for (feature in recipe.recipeFeatures)
                {
                    val featureObj = JSONObject()
                    featureObj.put("name", feature.name)
                    recipeFeatures.put(featureObj)
                }
                recipeObj.put("recipeFeatures", recipeFeatures)
                recipeObj.put("type", recipe.type)

                jsonArr.put(recipeObj)
            }

            jSonObj.put("recipes", jsonArr)

            return jSonObj
        }
    }
}