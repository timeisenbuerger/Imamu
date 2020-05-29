package com.github.tei.imamu.util

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class ShareUtil
{
    companion object
    {
        fun shareCookBook(cookBook: CookBook, activity: Activity)
        {
            val context = activity.baseContext
            val cookBookJson = JsonUtil.toJson(cookBook)

            try
            {
                if (!File(context.cacheDir.absolutePath + "/exports/").exists())
                {
                    File(context.cacheDir.absolutePath + "/exports/").mkdir()
                }

                val json = File(context.cacheDir.absolutePath + "/exports/", cookBook.title + ".json")
                if (json.exists())
                {
                    json.delete()
                    json.createNewFile()
                }
                else
                {
                    json.createNewFile()
                }

                val bufferedWriter = BufferedWriter(FileWriter(json))
                bufferedWriter.write(cookBookJson.toString())
                bufferedWriter.flush()
                bufferedWriter.close()

                val contentUri = FileProvider.getUriForFile(context, "com.github.tei.imamu.fileprovider", json)
                val shareIntent = ShareCompat.IntentBuilder.from(activity)
                    .setStream(contentUri) // uri from FileProvider
                    .setType("application/json").intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                activity.startActivity(Intent.createChooser(shareIntent, "Kochbuch teilen"))
            }
            catch (exception: Exception)
            {
                Toast.makeText(context, "Etwas ist bei dem Teilen des gewählten Elements fehlgeschlagen.", Toast.LENGTH_LONG)
                    .show()
            }
        }

        fun shareRecipe(recipe: Recipe, activity: Activity)
        {
            val text = collectRecipeTexts(recipe)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Rezept teilen")
            activity.startActivity(shareIntent)
        }

        private fun collectRecipeTexts(recipe: Recipe): String
        {
            // ich liebe dich :)
            var text = recipe.title + "\n\n"
            text += "Zutaten: " + recipe.servingsNumber + "\n\n"
            text += "Zutaten:\n"
            for (ingredient in recipe.recipeIngredients)
            {
                text += ingredient.amount + "   " + ingredient.unit + "   " + ingredient.ingredient.target.name + "\n"
            }
            text += "\n" + "Zubereitung:\n" + recipe.preparation

            return text
        }
    }
}