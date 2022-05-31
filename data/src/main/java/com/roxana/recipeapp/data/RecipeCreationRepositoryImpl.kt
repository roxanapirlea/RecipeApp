package com.roxana.recipeapp.data

import androidx.datastore.core.DataStore
import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeCreationRepositoryImpl @Inject constructor(
    private val recipeDataStore: DataStore<RecipeCreation>
) : RecipeCreationRepository {

    override fun getTitle(): Flow<String?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasTitle()) recipe.title else null
        }

    override fun getCategories(): Flow<Set<CategoryType>> =
        recipeDataStore.data.map { recipe ->
            recipe.categoriesList
                .mapNotNull { it.toDomainModel() }
                .toSet()
        }

    override fun getPortions(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasPortions()) recipe.portions.toShort() else null
        }

    override fun getInstructions(): Flow<List<CreationInstruction>> =
        recipeDataStore.data.map { recipe ->
            recipe.instructionsList
                .map { CreationInstruction(it.name, it.ordinal.toShort()) }
        }

    override fun getIngredients(): Flow<List<CreationIngredient>> =
        recipeDataStore.data.map { recipe ->
            recipe.ingredientsList
                .map {
                    val id = if (it.hasId()) it.id else null
                    val quantity =
                        if (it.hasQuantity()) it.quantity else null
                    val quantityType =
                        if (it.hasQuantityType()) it.quantityType.toDomainModel() else null
                    CreationIngredient(id, it.name, quantity, quantityType)
                }
        }

    override fun getTimeTotal(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasTotalTime()) recipe.totalTime.toShort() else null
        }

    override fun getTimePreparation(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasPreparationTime()) recipe.preparationTime.toShort() else null
        }

    override fun getTimeCooking(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasCookingTime()) recipe.cookingTime.toShort() else null
        }

    override fun getTimeWaiting(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasWaitingTime()) recipe.waitingTime.toShort() else null
        }

    override fun getTemperature(): Flow<Short?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasTemperature()) recipe.temperature.toShort() else null
        }

    override fun getTemperatureUnit(): Flow<Temperature?> =
        recipeDataStore.data.map { recipe ->
            if (recipe.hasTemperatureUnit()) recipe.temperatureUnit.toDomainModel() else null
        }

    override fun getRecipe(): Flow<CreationRecipe> =
        recipeDataStore.data.map { recipe ->
            val id = if (recipe.hasId()) recipe.id else null
            val title = recipe.title
            val portions = if (recipe.hasPortions()) recipe.portions.toShort() else null
            val categories = recipe.categoriesList
                .mapNotNull { it.toDomainModel() }
            val ingredients = recipe.ingredientsList
                .map {
                    val ingredientId = if (it.hasId()) it.id else null
                    val quantity =
                        if (it.hasQuantity()) it.quantity else null
                    val quantityType =
                        if (it.hasQuantityType()) it.quantityType.toDomainModel() else null
                    CreationIngredient(ingredientId, it.name, quantity, quantityType)
                }
            val instructions = recipe.instructionsList
                .map { CreationInstruction(it.name, it.ordinal.toShort()) }
            val totalTime = if (recipe.hasTotalTime()) recipe.totalTime.toShort() else null
            val cookingTime = if (recipe.hasCookingTime()) recipe.cookingTime.toShort() else null
            val preparationTime =
                if (recipe.hasPreparationTime()) recipe.preparationTime.toShort() else null
            val waitingTime = if (recipe.hasWaitingTime()) recipe.waitingTime.toShort() else null
            val temperature = if (recipe.hasTemperature()) recipe.temperature.toShort() else null
            val temperatureUnit =
                if (recipe.hasTemperatureUnit()) recipe.temperatureUnit.toDomainModel() else null
            CreationRecipe(
                id,
                title,
                null,
                portions,
                categories,
                instructions,
                ingredients,
                totalTime,
                preparationTime,
                cookingTime,
                waitingTime,
                temperature,
                temperatureUnit ?: Temperature.CELSIUS,
                emptyList()
            )
        }

    override suspend fun setTitle(title: String?) {
        recipeDataStore.updateData { current ->
            current.toBuilder().apply {
                if (title != null) setTitle(title) else clearTitle()
            }.build()
        }
    }

    override suspend fun setCategories(categories: Set<CategoryType>) {
        recipeDataStore.updateData { current ->
            current.toBuilder()
                .clearCategories()
                .addAllCategories(categories.map { it.toProto() })
                .build()
        }
    }

    override suspend fun setPortions(portionCount: Short?) {
        recipeDataStore.updateData { current ->
            current.toBuilder().apply {
                if (portionCount != null) portions = portionCount.toInt() else clearPortions()
            }.build()
        }
    }

    override suspend fun setInstructions(instructions: List<CreationInstruction>) {
        recipeDataStore.updateData { current ->
            current.toBuilder()
                .clearInstructions()
                .addAllInstructions(
                    instructions.map {
                        RecipeCreation.Indexed.newBuilder()
                            .setName(it.name)
                            .setOrdinal(it.ordinal.toInt())
                            .build()
                    }
                )
                .build()
        }
    }

    override suspend fun setIngredients(ingredients: List<CreationIngredient>) {
        recipeDataStore.updateData { current ->
            current.toBuilder()
                .clearIngredients()
                .addAllIngredients(
                    ingredients.map { ingredient ->
                        RecipeCreation.Ingredient.newBuilder()
                            .setName(ingredient.name)
                            .apply { ingredient.quantity?.let { quantity = it } }
                            .apply {
                                ingredient.quantityType?.let {
                                    quantityType = it.toCreationProto()
                                }
                            }
                            .apply { ingredient.id?.let { id = it } }
                            .build()
                    }
                )
                .build()
        }
    }

    override suspend fun setTime(
        cooking: Short?,
        preparation: Short?,
        waiting: Short?,
        total: Short?
    ) {
        recipeDataStore.updateData { current ->
            current.toBuilder()
                .apply {
                    if (total != null) totalTime = total.toInt() else clearTotalTime()
                }.apply {
                    if (preparation != null) preparationTime = preparation.toInt()
                    else clearPreparationTime()
                }.apply {
                    if (cooking != null) cookingTime = cooking.toInt() else clearCookingTime()
                }.apply {
                    if (waiting != null) waitingTime = waiting.toInt() else clearWaitingTime()
                }
                .build()
        }
    }

    override suspend fun setTemperature(temperature: Short?, temperatureUnit: Temperature) {
        recipeDataStore.updateData { current ->
            current.toBuilder()
                .setTemperatureUnit(temperatureUnit.toCreationProto())
                .apply {
                    if (temperature != null)
                        this.temperature = temperature.toInt()
                    else clearTemperature()
                }.build()
        }
    }

    override suspend fun reset() {
        recipeDataStore.updateData {
            RecipeCreation.newBuilder().build()
        }
    }
}
