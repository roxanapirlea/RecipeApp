package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class RecipeTest {

    private lateinit var queries: RecipeQueries

    @Before
    fun setUp() {
        queries = createInMemoryDb().recipeQueries
    }

    @Test
    fun returnEmptyList_when_emptyDb() {
        // When
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun returnInsertedOrderedItems_when_insertAndSelect() {
        // Given
        val recipeC = Recipe(2, "Crepe", null, null, null, null, null, null)
        val recipeD = Recipe(1, "Donut", null, null, null, null, null, null)

        // When
        queries.insert(
            recipeD.name,
            recipeD.photo_path,
            recipeD.time_total,
            recipeD.time_preparation,
            recipeD.time_cooking,
            recipeD.time_waiting,
            recipeD.temperature
        )
        queries.insert(
            recipeC.name,
            recipeC.photo_path,
            recipeC.time_total,
            recipeC.time_preparation,
            recipeC.time_cooking,
            recipeC.time_waiting,
            recipeC.temperature
        )
        val output = queries.getAll().executeAsList()

        // Then
        output.first().shouldBeEqualToIgnoringFields(recipeC, Recipe::id)
        output.last().shouldBeEqualToIgnoringFields(recipeD, Recipe::id)
    }

    @Test
    fun returnUpdatedItem_when_Update() {
        // Given
        val recipe = Recipe(1, "Crepe", null, null, null, null, null, null)
        val recipeUpdated = Recipe(1, "Donut", "path", 1, 2, 3, 4, 5)
        queries.insert(
            recipe.name,
            recipe.photo_path,
            recipe.time_total,
            recipe.time_preparation,
            recipe.time_cooking,
            recipe.time_waiting,
            recipe.temperature
        )

        // When
        queries.update(
            recipeUpdated.name,
            recipeUpdated.photo_path,
            recipeUpdated.time_total,
            recipeUpdated.time_cooking,
            recipeUpdated.time_preparation,
            recipeUpdated.time_waiting,
            recipeUpdated.temperature,
            recipeUpdated.id
        )
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldContain(recipeUpdated)
        output.shouldNotContain(recipe)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert("Donut", "path", 1, 2, 3, 4, 5)
        val initialRecipe =
            queries.getAll().executeAsList().first { it.name == "Donut" }

        // When
        queries.delete(initialRecipe.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }
}
