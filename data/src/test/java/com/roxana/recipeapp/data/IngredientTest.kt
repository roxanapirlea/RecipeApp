package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class IngredientTest {

    private lateinit var queries: IngredientQueries

    @Before
    fun setUp() {
        queries = createInMemoryDb().ingredientQueries
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
        val ingredient1 = Ingredient(1, "ingredient1")
        val ingredient2 = Ingredient(2, "ingredient2")

        // When
        queries.insert(ingredient1.name)
        queries.insert(ingredient2.name)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(ingredient1, Ingredient::id)
        output.last().shouldBeEqualToIgnoringFields(ingredient2, Ingredient::id)
    }

    @Test
    fun doNothing_when_insertDuplicate() {
        // Given
        queries.insert("ingredient")
        val initialIngredient = queries.getAll().executeAsList()

        // When
        queries.insert("ingredient")
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 1
        output.shouldBe(initialIngredient)
    }

    @Test
    fun returnItem_when_getById() {
        // Given
        queries.insert("ingredient")
        val ingredient = queries.getAll().executeAsList().first { it.name == "ingredient" }

        // When
        val output = queries.getById(ingredient.id).executeAsOne()

        // Then
        output.shouldBe(ingredient)
    }
}
