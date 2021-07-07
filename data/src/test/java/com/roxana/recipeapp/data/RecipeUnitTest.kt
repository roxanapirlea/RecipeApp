package com.roxana.recipeapp.data

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import org.junit.Test

class RecipeUnitTest {
    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        Database.Schema.create(this)
    }

    private val queries = Database(inMemorySqlDriver).recipeQueries

    @Test
    fun returnEmptyList_when_emptyDb() {
        // When
        val output = queries.selectAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun returnInsertedOrderedItems_when_insertAndSelect() {
        // Given
        val recipeC = Recipe(2, "Crepe")
        val recipeD = Recipe(1, "Donut")
        queries.insertOrReplace(recipeC.id, recipeC.name)
        queries.insertOrReplace(recipeD.id, recipeD.name)

        // When
        val output = queries.selectAll().executeAsList()

        // Then
        output.shouldContainInOrder(recipeC, recipeD)
    }
}
