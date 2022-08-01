package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.Instruction
import com.roxana.recipeapp.data.InstructionQueries
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class InstructionTest {

    private lateinit var queries: InstructionQueries
    private lateinit var db: Database
    private val recipes = listOf(recipe1, recipe2)

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.instructionQueries
        db.insertFakeRecipes(recipes)
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
        val instruction1 = Instruction(1, "instruction1", 10, recipes[0].id)
        val instruction2 = Instruction(2, "instruction2", 20, recipes[0].id)

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
        queries.insert("instruction1", 10, recipes[0].id)
        val initialComm =
            queries.getAll().executeAsList().first { it.details == "instruction1" }

        // When
        queries.update("instruction2", 20, initialComm.id)
        val output = queries.getAll().executeAsList()

        // Then
        val expectedComm = Instruction(initialComm.id, "instruction2", 20, recipes[0].id)
        output shouldHaveSize 1
        output.first().shouldBeEqualToIgnoringFields(expectedComm, Instruction::id)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert("instruction1", 10, recipes[0].id)
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
        queries.insert("instruction1", 10, recipes[0].id)
        queries.insert("instruction2", 20, recipes[0].id)
        queries.insert("instruction3", 30, recipes[1].id)

        // When
        val output = queries.getByRecipeId(recipes[0].id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
