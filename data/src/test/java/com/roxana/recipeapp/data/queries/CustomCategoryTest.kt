package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.CategoryForRecipe
import com.roxana.recipeapp.data.CustomCategory
import com.roxana.recipeapp.data.CustomCategoryQueries
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class CustomCategoryTest {

    private lateinit var queries: CustomCategoryQueries

    @Before
    fun setUp() {
        queries = createInMemoryDb().customCategoryQueries
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
        val cat1 = CustomCategory(1, "cat1")
        val cat2 = CustomCategory(2, "cat2")

        // When
        queries.insert(cat1.name)
        queries.insert(cat2.name)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(cat1, CategoryForRecipe::id)
        output.last().shouldBeEqualToIgnoringFields(cat2, CategoryForRecipe::id)
    }

    @Test
    fun doNothing_when_insertDuplicate() {
        // Given
        queries.insert("cat1")
        val initialCategories = queries.getAll().executeAsList()

        // When
        queries.insert("cat1")
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 1
        output.shouldBe(initialCategories)
    }

    @Test
    fun returnItem_when_getById() {
        // Given
        queries.insert("cat1")
        val cat1 = queries.getAll().executeAsList().first { it.name == "cat1" }

        // When
        val output = queries.getById(cat1.id).executeAsOne()

        // Then
        output.shouldBe(cat1)
    }
}
