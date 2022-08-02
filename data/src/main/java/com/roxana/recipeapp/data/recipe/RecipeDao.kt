package com.roxana.recipeapp.data.recipe

import com.roxana.recipeapp.data.GetMaxCookingTime
import com.roxana.recipeapp.data.GetMaxPreparationTime
import com.roxana.recipeapp.data.GetMaxTotalTime
import com.roxana.recipeapp.data.GetRecipesSummary
import com.roxana.recipeapp.data.Recipe
import com.roxana.recipeapp.data.RecipeQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeDao @Inject constructor(
    private val queries: RecipeQueries
) {
    fun insert(
        name: String,
        photoPath: String?,
        portions: Short?,
        timeTotal: Short?,
        timeCooking: Short?,
        timePreparation: Short?,
        timeWaiting: Short?,
        temperature: Short?,
        temperatureUnit: DbTemperatureType?
    ) {
        queries.insert(
            name,
            photoPath,
            portions,
            timeTotal,
            timeCooking,
            timePreparation,
            timeWaiting,
            temperature,
            temperatureUnit
        )
    }

    fun update(
        name: String,
        photoPath: String?,
        portions: Short?,
        timeTotal: Short?,
        timeCooking: Short?,
        timePreparation: Short?,
        timeWaiting: Short?,
        temperature: Short?,
        temperatureUnit: DbTemperatureType?,
        id: Long
    ) {
        queries.update(
            name,
            photoPath,
            portions,
            timeTotal,
            timeCooking,
            timePreparation,
            timeWaiting,
            temperature,
            temperatureUnit,
            id,
        )
    }

    fun getByName(name: String): Recipe = queries.getByName(name).executeAsOne()

    fun getById(id: Long): Recipe = queries.getById(id).executeAsOne()

    fun getByIdAsFlow(id: Long): Flow<Recipe> = queries.getById(id).asFlow().mapToOne()

    fun getRecipesSummary(
        nameQuery: String,
        totalTime: Short?,
        cookingTime: Short?,
        preparationTime: Short?,
        category: DbCategoryType?
    ): Flow<List<GetRecipesSummary>> = queries.getRecipesSummary(
        totalTime,
        cookingTime,
        preparationTime,
        category,
        nameQuery
    )
        .asFlow()
        .mapToList()

    fun deleteById(id: Long) {
        queries.delete(id)
    }

    fun getMaxTotalTime(): Flow<GetMaxTotalTime> =
        queries.getMaxTotalTime().asFlow().mapToOne()

    fun getMaxCookingTime(): Flow<GetMaxCookingTime> =
        queries.getMaxCookingTime().asFlow().mapToOne()

    fun getMaxPreparationTime(): Flow<GetMaxPreparationTime> =
        queries.getMaxPreparationTime().asFlow().mapToOne()
}
