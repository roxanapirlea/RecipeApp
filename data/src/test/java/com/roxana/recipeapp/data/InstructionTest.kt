package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class InstructionTest {

    private lateinit var queries: InstructionQueries
    private lateinit var db: Database

    private val recipe = Recipe(1, "Crepe", null, null, null, null, null, null)
    private val recipe2 = Recipe(2, "Donut", null, null, null, null, null, null)

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.instructionQueries

        db.recipeQueries.insert(
            recipe.name,
            recipe.photo_path,
            recipe.time_total,
            recipe.time_preparation,
            recipe.time_cooking,
            recipe.time_waiting,
            recipe.temperature
        )
        db.recipeQueries.insert(
            recipe2.name,
            recipe2.photo_path,
            recipe2.time_total,
            recipe2.time_preparation,
            recipe2.time_cooking,
            recipe2.time_waiting,
            recipe2.temperature
        )
    }

    @Test
    fun returnEmptyList_when_emptyDb() {
        // When
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun returnInsertedItems_when_insertAndSelect() {
        // Given
        val instruction1 = Instruction(1, "instruction1", 10, recipe.id)
        val instruction2 = Instruction(2, "instruction2", 20, recipe.id)

        // When
        queries.insert(instruction1.details, instruction1.ordinal, instruction1.recipe_id)
        queries.insert(instruction2.details, instruction2.ordinal, instruction2.recipe_id)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(instruction1, Instruction::id)
        output.last().shouldBeEqualToIgnoringFields(instruction2, Instruction::id)
    }

    @Test
    fun returnUpdatedItem_when_Update() {
        // Given
        queries.insert("instruction1", 10, recipe.id)
        val initialComm =
            queries.getAll().executeAsList().first { it.details == "instruction1" }

        // When
        queries.update("instruction2", 20, initialComm.id)
        val output = queries.getAll().executeAsList()

        // Then
        val expectedComm = Instruction(initialComm.id, "instruction2", 20, recipe.id)
        output shouldHaveSize 1
        output.first().shouldBeEqualToIgnoringFields(expectedComm, Instruction::id)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert("instruction1", 10, recipe.id)
        val initialComm =
            queries.getAll().executeAsList().first { it.details == "instruction1" }

        // When
        queries.delete(initialComm.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert("instruction1", 10, recipe.id)
        queries.insert("instruction2", 20, recipe.id)
        queries.insert("instruction3", 30, recipe2.id)

        // When
        val output = queries.getByRecipeId(recipe.id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
