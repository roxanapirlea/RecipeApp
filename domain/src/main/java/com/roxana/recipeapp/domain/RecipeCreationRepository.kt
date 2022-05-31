package com.roxana.recipeapp.domain

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.flow.Flow

interface RecipeCreationRepository {
    fun getTitle(): Flow<String?>
    suspend fun setTitle(title: String?)
    fun getCategories(): Flow<Set<CategoryType>>
    suspend fun setCategories(categories: Set<CategoryType>)
    fun getPortions(): Flow<Short?>
    suspend fun setPortions(portionCount: Short?)
    fun getInstructions(): Flow<List<CreationInstruction>>
    suspend fun setInstructions(instructions: List<CreationInstruction>)
    fun getIngredients(): Flow<List<CreationIngredient>>
    suspend fun setIngredients(ingredients: List<CreationIngredient>)
    fun getTimeTotal(): Flow<Short?>
    fun getTimePreparation(): Flow<Short?>
    fun getTimeCooking(): Flow<Short?>
    fun getTimeWaiting(): Flow<Short?>
    suspend fun setTime(cooking: Short?, preparation: Short?, waiting: Short?, total: Short?)
    fun getTemperature(): Flow<Short?>
    fun getTemperatureUnit(): Flow<Temperature?>
    suspend fun setTemperature(temperature: Short?, temperatureUnit: Temperature)
    fun getRecipe(): Flow<CreationRecipe>
    suspend fun setRecipe(recipe: CreationRecipe)
    suspend fun reset()
}
