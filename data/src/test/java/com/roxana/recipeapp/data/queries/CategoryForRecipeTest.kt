package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.CategoryForRecipe
import com.roxana.recipeapp.data.CategoryForRecipeQueries
import com.roxana.recipeapp.data.CustomCategory
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.recipe.DbCategoryType
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class CategoryForRecipeTest {

    private lateinit var queries: CategoryForRecipeQueries
    private lateinit var db: Database
    private val recipes = listOf(recipe1, recipe2)

    private lateinit var customCategory: CustomCategory

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.categoryForRecipeQueries
        db.insertFakeRecipes(recipes)
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
        val standardCat = CategoryForRecipe(1, DbCategoryType.BREAKFAST, null, recipes[0].id)
        val customCat = CategoryForRecipe(1, null, customCategory.id, recipes[0].id)

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
    fun deleteItem_when_delete() {
        // Given
        queries.insert(DbCategoryType.BREAKFAST, null, recipes[0].id)
        val initialCat =
            queries.getAll().executeAsList().first { it.name == DbCategoryType.BREAKFAST }

        // When
        queries.delete(initialCat.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert(DbCategoryType.BREAKFAST, null, recipes[0].id)
        queries.insert(null, customCategory.id, recipes[0].id)
        queries.insert(DbCategoryType.DRINK, null, recipes[1].id)

        // When
        val output = queries.getCategoryByRecipeId(recipes[0].id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
