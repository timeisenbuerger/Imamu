package com.github.tei.imamu.util

import android.content.Context
import android.net.Uri
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.data.database.entity.recipe.RecipeIngredient
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class ImportUtil
{
    companion object
    {
        lateinit var viewModel: CookBookListViewModel
        fun importCookBook(viewModel: CookBookListViewModel, context: Context, data: Uri?)
        {
            this.viewModel = viewModel
            data?.let { uri ->
                val json = getFile(context, uri)
                var jsonContent = ""
                BufferedReader(FileReader(json)).lines()
                    .forEach {
                        jsonContent += it + "\n"
                    }

                val cookBookJson = JSONObject(jsonContent.trim())

                val newCookBook = createCookBook(cookBookJson)
                viewModel.saveNewCookBook(newCookBook)

                json.delete()
            }
        }

        private fun createCookBook(jsonObject: JSONObject) : CookBook
        {
            val newCookBook = CookBook()
            val keys: Iterator<String> = jsonObject.keys()
            while (keys.hasNext())
            {
                val key = keys.next()
                when (val value = jsonObject.get(key))
                {
                    is JSONArray ->
                    {
                        for (index in 0 until value.length())
                        {
                            val obj = value.get(index) as JSONObject
                            val recipe = createRecipe(obj)
                            newCookBook.recipes.add(recipe)
                        }
                    }
                    is String    ->
                    {
                        newCookBook.title = value
                    }
                }
            }
            return newCookBook
        }

        private fun createRecipe(jsonObject: JSONObject): Recipe
        {
            val recipe = Recipe()

            val keys: Iterator<String> = jsonObject.keys()
            while (keys.hasNext())
            {
                when (val key = keys.next())
                {
                    "title"             -> recipe.title = jsonObject.get(key) as String
                    "servingsNumber"    -> recipe.servingsNumber = jsonObject.get(key) as String
                    "preparation"       -> recipe.preparation = jsonObject.get(key) as String
                    "difficulty"        -> recipe.difficulty = jsonObject.get(key) as String
                    "preparationTime"   -> recipe.preparationTime = jsonObject.get(key) as String
                    "bakingTime"        -> recipe.bakingTime = jsonObject.get(key) as String
                    "restTime"          -> recipe.restTime = jsonObject.get(key) as String
                    "totalTime"         -> recipe.totalTime = jsonObject.get(key) as Int
                    "type"              -> recipe.type = jsonObject.get(key) as String
                    "nutrition"         -> recipe.nutrition = jsonObject.get(key) as String
                    "recipeIngredients" ->
                    {
                        val recipeIngredients = jsonObject.get(key) as JSONArray
                        for (index in 0 until recipeIngredients.length())
                        {
                            val obj = recipeIngredients.get(index) as JSONObject
                            val ingredient = createIngredient(obj)
                            recipe.recipeIngredients.add(ingredient)
                        }
                    }
                }
            }

            return recipe
        }

        private fun createIngredient(jsonObject: JSONObject): RecipeIngredient
        {
            val recipeIngredient = RecipeIngredient()
            val keys: Iterator<String> = jsonObject.keys()
            while (keys.hasNext())
            {
                when (val key = keys.next())
                {
                    "amount" -> recipeIngredient.amount = jsonObject.get(key) as String
                    "unit"   -> recipeIngredient.unit = jsonObject.get(key) as String
                    "name"   ->
                    {
                        val name = jsonObject.get(key) as String
                        recipeIngredient.ingredient.target = viewModel.ingredientRepository.getIngredientForName(name)
                    }
                }
            }

            return recipeIngredient
        }

        private fun getFile(context: Context, uri: Uri): File
        {
            val json = File(context.cacheDir.absolutePath + "/" + "temp.json")
            json.createNewFile()

            val input = context.contentResolver.openInputStream(uri)
            val output = FileOutputStream(json)
            val buf = ByteArray(1024)
            var len: Int
            while (input!!.read(buf)
                    .also { len = it } > 0
            )
            {
                output.write(buf, 0, len)
            }
            output.close()
            input.close()

            return json
        }
    }
}