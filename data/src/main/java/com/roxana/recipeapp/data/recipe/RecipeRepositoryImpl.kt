package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.GetCategoryByRecipeId
import com.roxana.recipeapp.data.GetIngredientByRecipeId
import com.roxana.recipeapp.data.GetRecipesSummary
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val ingredientForRecipeDao: IngredientForRecipeDao,
    private val categoryForRecipeDao: CategoryForRecipeDao,
    private val instructionDao: InstructionDao,
    private val commentDao: CommentDao
) : RecipeRepository {
    override suspend fun addRecipe(recipe: CreationRecipe) {
        recipeDao.insert(
            recipe.name,
            recipe.photoPath,
            recipe.portions,
            recipe.timeTotal,
            recipe.timeCooking,
            recipe.timePreparation,
            recipe.timeWaiting,
            recipe.temperature,
            recipe.temperatureUnit.toDataModel(),
        )
        val insertedRecipe = recipeDao.getByName(recipe.name)
        recipe.ingredients.forEach {
            ingredientDao.insert(it.name)
            val ingredient = ingredientDao.getByName(it.name)
            ingredientForRecipeDao.insert(
                it.quantity,
                it.quantityType.toDataModel(),
                ingredient.id,
                insertedRecipe.id
            )
        }
        recipe.categories.forEach {
            categoryForRecipeDao.insert(it.toDataModel(), insertedRecipe.id)
        }
        recipe.instructions.forEach {
            instructionDao.insert(it.name, it.ordinal, insertedRecipe.id)
        }
        recipe.comments.forEach {
            commentDao.insert(it.detail, it.ordinal, insertedRecipe.id)
        }
    }

    override suspend fun updateRecipe(recipe: CreationRecipe) {
        recipeDao.update(
            recipe.name,
            recipe.photoPath,
            recipe.portions,
            recipe.timeTotal,
            recipe.timeCooking,
            recipe.timePreparation,
            recipe.timeWaiting,
            recipe.temperature,
            recipe.temperatureUnit.toDataModel(),
            recipe.id!!.toLong(),
        )
        recipe.ingredients.forEach {
            ingredientDao.insert(it.name)
            val ingredient = ingredientDao.getByName(it.name)
            ingredientForRecipeDao.deleteByRecipeId(recipe.id!!.toLong())
            ingredientForRecipeDao.insert(
                it.quantity,
                it.quantityType.toDataModel(),
                ingredient.id,
                recipe.id!!.toLong()
            )
        }
        val existingCategories = categoryForRecipeDao
            .getByRecipeId(recipe.id!!.toLong())
            .filter { it.name != null }
            .associate { it.id to it.name?.toDomainModel() }

        existingCategories
            .filterNot { recipe.categories.contains(it.value) }
            .forEach {
                categoryForRecipeDao.delete(it.key)
            }
        recipe.categories
            .filterNot { existingCategories.containsValue(it) }
            .forEach {
                categoryForRecipeDao.insert(it.toDataModel(), recipe.id!!.toLong())
            }
        instructionDao.deleteByRecipeId(recipe.id!!.toLong())
        recipe.instructions.forEach {
            instructionDao.insert(it.name, it.ordinal, recipe.id!!.toLong())
        }
        commentDao.deleteByRecipeId(recipe.id!!.toLong())
        recipe.comments.forEach {
            commentDao.insert(it.detail, it.ordinal, recipe.id!!.toLong())
        }
    }

    override fun getRecipesSummary(
        nameQuery: String,
        totalTime: Short?,
        cookingTime: Short?,
        preparationTime: Short?,
        category: CategoryType?
    ): Flow<List<RecipeSummary>> {
        return recipeDao.getRecipesSummary(
            nameQuery,
            totalTime,
            cookingTime,
            preparationTime,
            category?.toDataModel()
        ).map(::mapSummary)
    }

    private fun mapSummary(dbSummaries: List<GetRecipesSummary>): List<RecipeSummary> =
        dbSummaries
            .groupBy { it.id to it.name }
            .map { (pairIdName, summary) ->
                val categories = summary.mapNotNull { it.category?.toDomainModel() }
                val photoPath = summary.first().photo_path
                RecipeSummary(pairIdName.first.toInt(), pairIdName.second, photoPath, categories)
            }

    override suspend fun getRecipeById(id: Int): Recipe {
        val recipe = recipeDao.getById(id.toLong())
        val categories = categoryForRecipeDao.getByRecipeId(id.toLong())
        val ingredients = ingredientForRecipeDao.getByRecipeId(id.toLong())
        val instructions = instructionDao.getByRecipeId(id.toLong())
        val comments = commentDao.getByRecipeId(id.toLong())
        return mapRecipe(recipe, categories, ingredients, instructions, comments)
    }

    override fun getRecipeByIdAsFlow(id: Int): Flow<Recipe> {
        return combine(
            recipeDao.getByIdAsFlow(id.toLong()),
            categoryForRecipeDao.getByRecipeIdAsFlow(id.toLong()),
            ingredientForRecipeDao.getByRecipeIdAsFlow(id.toLong()),
            instructionDao.getByRecipeIdAsFlow(id.toLong()),
            commentDao.getByRecipeIdAsFlow(id.toLong()),
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
            photoPath = dataRecipe.photo_path,
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
        val comments = commentDao.getByRecipeId(recipeId.toLong())
        val nextOrdinal = (comments.maxOfOrNull { it.ordinal } ?: 0) + 1
        commentDao.insert(comment, nextOrdinal.toShort(), recipeId.toLong())
    }

    override fun getMaxTotalTime(): Flow<Short?> {
        return recipeDao.getMaxTotalTime().map { it.MAX?.toShort() }
    }

    override fun getMaxCookingTime(): Flow<Short?> {
        return recipeDao.getMaxCookingTime().map { it.MAX?.toShort() }
    }

    override fun getMaxPreparationTime(): Flow<Short?> {
        return recipeDao.getMaxPreparationTime().map { it.MAX?.toShort() }
    }

    override suspend fun deleteById(id: Int) {
        recipeDao.deleteById(id.toLong())
    }
}
