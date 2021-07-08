package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class CategoryForRecipeTest {

    private lateinit var queries: CategoryForRecipeQueries
    private lateinit var db: Database

    private lateinit var customCategory: CustomCategory
    private val recipe = Recipe(1, "Crepe", null, null, null, null, null, null)
    private val recipe2 = Recipe(2, "Donut", null, null, null, null, null, null)

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.categoryForRecipeQueries
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
        db.customCategoryQueries.insert("custom")
        customCategory =
            db.customCategoryQueries.getAll().executeAsList().first { it.name == "custom" }
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
        val standardCat = CategoryForRecipe(1, CategoryType.BREAKFAST, null, recipe.id)
        val customCat = CategoryForRecipe(1, null, customCategory.id, recipe.id)

        // When
        queries.insert(standardCat.name, standardCat.category_id, standardCat.recipe_id)
        queries.insert(customCat.name, customCat.category_id, customCat.recipe_id)
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 2
        output.first().shouldBeEqualToIgnoringFields(standardCat, CategoryForRecipe::id)
        output.last().shouldBeEqualToIgnoringFields(customCat, CategoryForRecipe::id)
    }

    @Test
    fun returnUpdatedItem_when_Update() {
        // Given
        queries.insert(CategoryType.BREAKFAST, null, recipe.id)
        val initialCat =
            queries.getAll().executeAsList().first { it.name == CategoryType.BREAKFAST }

        // When
        queries.update(null, customCategory.id, initialCat.id)
        val output = queries.getAll().executeAsList()

        // Then
        val expectedCat = CategoryForRecipe(initialCat.id, null, customCategory.id, recipe.id)
        output shouldHaveSize 1
        output.first().shouldBeEqualToIgnoringFields(expectedCat, CategoryForRecipe::id)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert(CategoryType.BREAKFAST, null, recipe.id)
        val initialCat =
            queries.getAll().executeAsList().first { it.name == CategoryType.BREAKFAST }

        // When
        queries.delete(initialCat.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert(CategoryType.BREAKFAST, null, recipe.id)
        queries.insert(null, customCategory.id, recipe.id)
        queries.insert(CategoryType.DRINK, null, recipe2.id)

        // When
        val output = queries.getCategoryByRecipeId(recipe.id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
