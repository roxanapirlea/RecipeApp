package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.GetIngredientByRecipeId
import com.roxana.recipeapp.data.IngredientForRecipeQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IngredientForRecipeDao @Inject constructor(
    private val queries: IngredientForRecipeQueries
) {
    fun insert(
        quantity: Double?,
        quantityType: DbQuantityType?,
        ingredientId: Long,
        recipeId: Long
    ) {
        queries.insert(quantity, quantityType, null, ingredientId, recipeId)
    }

    fun deleteByRecipeId(recipeId: Long) {
        queries.deleteByRecipeId(recipeId)
    }

    fun getByRecipeId(recipeId: Long): List<GetIngredientByRecipeId> =
        queries.getIngredientByRecipeId(recipeId).executeAsList()

    fun getByRecipeIdAsFlow(recipeId: Long): Flow<List<GetIngredientByRecipeId>> =
        queries.getIngredientByRecipeId(recipeId).asFlow().mapToList()
}
