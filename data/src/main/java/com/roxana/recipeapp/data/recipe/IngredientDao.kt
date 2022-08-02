package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.Ingredient
import com.roxana.recipeapp.data.IngredientQueries
import javax.inject.Inject

class IngredientDao @Inject constructor(
    private val queries: IngredientQueries
) {
    fun insert(name: String) {
        queries.insert(name)
    }

    fun getByName(name: String): Ingredient = queries.getByName(name).executeAsOne()
}
