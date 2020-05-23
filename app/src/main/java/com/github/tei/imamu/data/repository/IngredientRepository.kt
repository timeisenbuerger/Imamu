package com.github.tei.imamu.data.repository

import com.github.tei.imamu.data.database.entity.Ingredient
import com.github.tei.imamu.data.database.entity.Ingredient_
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor

class IngredientRepository(store: BoxStore) : BaseRepository<Ingredient>(store)
{
    private val ingredientBox = boxStore.boxFor<Ingredient>()

    override fun getAll(): ObjectBoxLiveData<Ingredient>
    {
        return ObjectBoxLiveData(ingredientBox.query()
            .build()
        )
    }

    fun getIngredientForName(name: String): Ingredient
    {
        val ingredient = ingredientBox.query()
            .equal(Ingredient_.name, name)
            .build()
            .find()

        return if (ingredient.isEmpty())
        {
            val newIngredient = Ingredient(name = name)
            newIngredient
        }
        else
        {
            ingredient[0]
        }
    }
}