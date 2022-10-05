package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.Comment
import com.roxana.recipeapp.data.CommentQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentDao @Inject constructor(
    private val queries: CommentQueries
) {
    fun insert(details: String, ordinal: Short, recipeId: Long) {
        queries.insert(details, ordinal, recipeId)
    }

    fun getByRecipeId(recipeId: Long): List<Comment> =
        queries.getByRecipeId(recipeId).executeAsList()

    fun getByRecipeIdAsFlow(recipeId: Long): Flow<List<Comment>> =
        queries.getByRecipeId(recipeId).asFlow().mapToList()

    fun deleteByRecipeId(recipeId: Long) {
        queries.deleteByRecipeId(recipeId)
    }

    fun deleteByRecipeAndOrdinal(recipeId: Long, ordinal: Short) {
        queries.deleteByRecipeIdAndOrdinal(recipeId, ordinal)
    }
}
