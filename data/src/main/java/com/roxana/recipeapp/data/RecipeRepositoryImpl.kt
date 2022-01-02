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
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
                temperature,
                temperatureUnit.toDataModel()
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
        val categories = categoryForRecipeQueries.getCategoryByRecipeId(id.toLong()).executeAsList()
        val ingredients = ingredientForRecipeQueries.getIngredientByRecipeId(id.toLong())
            .executeAsList()
        val instructions = instructionQueries.getByRecipeId(id.toLong()).executeAsList()
        val comments = commentQueries.getByRecipeId(id.toLong()).executeAsList()
        return mapRecipe(recipe, categories, ingredients, instructions, comments)
    }

    override fun getRecipeByIdAsFlow(id: Int): Flow<Recipe> {
        return combine(
            recipeQueries.getById(id.toLong()).asFlow().mapToOne(),
            categoryForRecipeQueries.getCategoryByRecipeId(id.toLong()).asFlow().mapToList(),
            ingredientForRecipeQueries.getIngredientByRecipeId(id.toLong()).asFlow().mapToList(),
            instructionQueries.getByRecipeId(id.toLong()).asFlow().mapToList(),
            commentQueries.getByRecipeId(id.toLong()).asFlow().mapToList(),
            ::mapRecipe
        )
    }

    private fun mapRecipe(
        dataRecipe: com.roxana.recipeapp.data.Recipe,
        dataCategories: List<GetCategoryByRecipeId>,
        dataIngredients: List<GetIngredientByRecipeId>,
        dataInstructions: List<com.roxana.recipeapp.data.Instruction>,
        dataComments: List<com.roxana.recipeapp.data.Comment>
    ): Recipe {
        val categories = dataCategories.mapNotNull { it.name?.toDomainModel() }
        val ingredients = dataIngredients.map {
            Ingredient(
                it.id.toInt(),
                it.ingredient_name,
                it.quantity,
                it.quantity_name.toDomainModel()
            )
        }
        val instructions = dataInstructions.map { Instruction(it.ordinal, it.details) }
        val comments = dataComments.map { Comment(it.ordinal, it.name) }
        return Recipe(
            id = dataRecipe.id.toInt(),
            name = dataRecipe.name,
            portions = dataRecipe.portions,
            categories = categories,
            ingredients = ingredients,
            instructions = instructions,
            comments = comments,
            timeTotal = dataRecipe.time_total,
            timeCooking = dataRecipe.time_cooking,
            timeWaiting = dataRecipe.time_waiting,
            timePreparation = dataRecipe.time_preparation,
            temperature = dataRecipe.temperature,
            temperatureUnit = dataRecipe.temperature_type.toDomainModel()
        )
    }

    override suspend fun addComment(recipeId: Int, comment: String) {
        val comments = commentQueries.getByRecipeId(recipeId.toLong()).executeAsList()
        val nextOrdinal = comments.maxOf { it.ordinal } + 1
        commentQueries.insert(comment, nextOrdinal.toShort(), recipeId.toLong())
    }
}
