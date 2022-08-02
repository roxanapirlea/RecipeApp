package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.CategoryForRecipeQueries
import com.roxana.recipeapp.data.GetCategoryByRecipeId
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryForRecipeDao @Inject constructor(
    private val queries: CategoryForRecipeQueries
) {
    fun insert(type: DbCategoryType, recipeId: Long) {
        queries.insert(type, null, recipeId)
    }

    fun getByRecipeId(recipeId: Long): List<GetCategoryByRecipeId> = queries
        .getCategoryByRecipeId(recipeId)
        .executeAsList()

    fun getByRecipeIdAsFlow(recipeId: Long): Flow<List<GetCategoryByRecipeId>> = queries
        .getCategoryByRecipeId(recipeId)
        .asFlow()
        .mapToList()

    fun delete(id: Long) {
        queries.delete(id)
    }
}
