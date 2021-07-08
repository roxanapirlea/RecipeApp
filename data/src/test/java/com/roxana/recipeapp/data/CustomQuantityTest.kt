package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class CustomQuantityTest {

    private lateinit var queries: CustomQuantityTypeQueries

    @Before
    fun setUp() {
        queries = createInMemoryDb().customQuantityTypeQueries
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
        val quantity1 = CustomQuantityType(1, "quantity1")
        val quantity2 = CustomQuantityType(2, "quantity2")

        // When
        queries.insert(quantity1.name)
        queries.insert(quantity2.name)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(quantity1, CustomQuantityType::id)
        output.last().shouldBeEqualToIgnoringFields(quantity2, CustomQuantityType::id)
    }

    @Test
    fun doNothing_when_insertDuplicate() {
        // Given
        queries.insert("quantity1")
        val initialQuantityTypes = queries.getAll().executeAsList()

        // When
        queries.insert("quantity1")
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 1
        output.shouldBe(initialQuantityTypes)
    }

    @Test
    fun returnItem_when_getById() {
        // Given
        queries.insert("quantity1")
        val quantity1 = queries.getAll().executeAsList().first { it.name == "quantity1" }

        // When
        val output = queries.getById(quantity1.id).executeAsOne()

        // Then
        output.shouldBe(quantity1)
    }
}
