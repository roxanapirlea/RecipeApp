package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class CommentTest {

    private lateinit var queries: CommentQueries
    private lateinit var db: Database

    private val recipe = Recipe(1, "Crepe", null, null, null, null, null, null, null)
    private val recipe2 = Recipe(2, "Donut", null, null, null, null, null, null, null)

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.commentQueries

        db.recipeQueries.insert(
            recipe.name,
            recipe.photo_path,
            recipe.portions,
            recipe.time_total,
            recipe.time_preparation,
            recipe.time_cooking,
            recipe.time_waiting,
            recipe.temperature
        )
        db.recipeQueries.insert(
            recipe2.name,
            recipe2.photo_path,
            recipe2.portions,
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
        val comm1 = Comment(1, "comm1", 10, recipe.id)
        val comm2 = Comment(2, "comm2", 20, recipe.id)

        // When
        queries.insert(comm1.name, comm1.ordinal, comm1.recipe_id)
        queries.insert(comm2.name, comm2.ordinal, comm2.recipe_id)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(comm1, Comment::id)
        output.last().shouldBeEqualToIgnoringFields(comm2, Comment::id)
    }

    @Test
    fun returnUpdatedItem_when_Update() {
        // Given
        queries.insert("comm1", 10, recipe.id)
        val initialComm =
            queries.getAll().executeAsList().first { it.name == "comm1" }

        // When
        queries.update("comm2", 20, initialComm.id)
        val output = queries.getAll().executeAsList()

        // Then
        val expectedComm = Comment(initialComm.id, "comm2", 20, recipe.id)
        output shouldHaveSize 1
        output.first().shouldBeEqualToIgnoringFields(expectedComm, Comment::id)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert("comm1", 10, recipe.id)
        val initialComm =
            queries.getAll().executeAsList().first { it.name == "comm1" }

        // When
        queries.delete(initialComm.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert("comm1", 10, recipe.id)
        queries.insert("comm2", 20, recipe.id)
        queries.insert("comm3", 30, recipe2.id)

        // When
        val output = queries.getByRecipeId(recipe.id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
