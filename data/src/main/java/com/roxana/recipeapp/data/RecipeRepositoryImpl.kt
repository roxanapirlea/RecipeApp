package com.roxana.recipeapp.data

import androidx.annotation.VisibleForTesting
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.Recipe
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

    override suspend fun getRecipeById(id: Int): Recipe {
        val recipe = recipeQueries.getById(id.toLong()).executeAsOne()
        val categories = categoryForRecipeQueries.getCategoryByRecipeId(id.toLong())
            .executeAsList()
            .mapNotNull { it.name?.toDomainModel() }
        val ingredients = ingredientForRecipeQueries.getIngredientByRecipeId(id.toLong())
            .executeAsList()
            .map {
                Ingredient(
                    it.id.toInt(),
                    it.ingredient_name,
                    it.quantity,
                    it.quantity_name.toDomainModel()
                )
            }
        val instructions = instructionQueries.getByRecipeId(id.toLong())
            .executeAsList()
            .map { Instruction(it.ordinal, it.details) }
        val comments = commentQueries.getByRecipeId(id.toLong())
            .executeAsList()
            .map { Comment(it.ordinal, it.name) }
        return Recipe(
            id = recipe.id.toInt(),
            name = recipe.name,
            portions = recipe.portions,
            categories = categories,
            ingredients = ingredients,
            instructions = instructions,
            comments = comments,
            timeTotal = recipe.time_total,
            timeCooking = recipe.time_cooking,
            timeWaiting = recipe.time_waiting,
            timePreparation = recipe.time_preparation,
            temperature = recipe.temperature
        )
    }

    override suspend fun addComment(recipeId: Int, comment: String) {
        val comments = commentQueries.getByRecipeId(recipeId.toLong()).executeAsList()
        val nextOrdinal = comments.maxOf { it.ordinal } + 1
        commentQueries.insert(comment, nextOrdinal.toShort(), recipeId.toLong())
    }
}
