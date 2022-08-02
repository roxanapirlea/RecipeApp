package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.Instruction
import com.roxana.recipeapp.data.InstructionQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InstructionDao @Inject constructor(
    private val queries: InstructionQueries
) {
    fun insert(details: String, ordinal: Short, recipeId: Long) {
        queries.insert(details, ordinal, recipeId)
    }

    fun getByRecipeId(recipeId: Long): List<Instruction> =
        queries.getByRecipeId(recipeId).executeAsList()

    fun getByRecipeIdAsFlow(recipeId: Long): Flow<List<Instruction>> =
        queries.getByRecipeId(recipeId).asFlow().mapToList()

    fun deleteByRecipeId(recipeId: Long) {
        queries.deleteByRecipeId(recipeId)
    }
}
