package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.Comment
import com.roxana.recipeapp.data.CommentQueries
import com.roxana.recipeapp.data.Database
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class CommentTest {

    private lateinit var queries: CommentQueries
    private lateinit var db: Database
    private val recipes = listOf(recipe1, recipe2)

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.commentQueries
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
        val comm1 = Comment(1, "comm1", 10, recipes[0].id)
        val comm2 = Comment(2, "comm2", 20, recipes[0].id)

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
    fun deleteItemsForRecipe_when_delete() {
        // Given
        queries.insert("comm1", 10, recipes[0].id)

        // When
        queries.deleteByRecipeId(recipes[0].id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert("comm1", 10, recipes[0].id)
        queries.insert("comm2", 20, recipes[0].id)
        queries.insert("comm3", 30, recipes[1].id)

        // When
        val output = queries.getByRecipeId(recipes[0].id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
