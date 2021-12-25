package com.roxana.recipeapp.data

import androidx.annotation.VisibleForTesting
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.RecipeSummary
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeQueries: RecipeQueries,
    private val ingredientQueries: IngredientQueries,
    private val ingredientForRecipeQueries: IngredientForRecipeQueries,
    private val categoryForRecipeQueries: CategoryForRecipeQueries,
    private val instructionQueries: InstructionQueries,
    private val commentQueries: CommentQueries
) : RecipeRepository {
    override suspend fun addRecipe(recipe: CreationRecipe) {
        with(recipe) {
            recipeQueries.insert(
                name,
                photoPath,
                portions,
                timeTotal,
                timeCooking,
                timePreparation,
                timeWaiting,
                temperature
            )
            val insertedRecipe = recipeQueries.getByName(name).executeAsOne()
            ingredients.forEach {
                ingredientQueries.insert(it.name)
                val ingredient = ingredientQueries.getByName(it.name).executeAsOne()
                ingredientForRecipeQueries.insert(
                    it.quantity,
                    it.quantityType.toDataModel(),
                    null,
                    ingredient.id,
                    insertedRecipe.id
                )
            }
            categories.forEach {
                categoryForRecipeQueries.insert(
                    it.toDataModel(),
                    null,
                    insertedRecipe.id
                )
            }
            instructions.forEach {
                instructionQueries.insert(it.name, it.ordinal, insertedRecipe.id)
            }
            comments.forEach {
                commentQueries.insert(it.detail, it.ordinal, insertedRecipe.id)
            }
        }
    }

    override fun getRecipesSummary(): Flow<List<RecipeSummary>> {
        return recipeQueries.getRecipesSummary()
            .asFlow()
            .mapToList()
            .map(::mapSummary)
    }

    @VisibleForTesting
    fun mapSummary(dbSummaries: List<GetRecipesSummary>): List<RecipeSummary> =
        dbSummaries
            .groupBy { it.id to it.name }
            .map { (pairIdName, summary) ->
                val categories = summary.mapNotNull { it.category?.toDomainModel() }
                RecipeSummary(pairIdName.first.toInt(), pairIdName.second, categories)
            }
}
