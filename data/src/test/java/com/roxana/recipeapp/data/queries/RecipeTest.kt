package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.CategoryForRecipeQueries
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.Recipe
import com.roxana.recipeapp.data.RecipeQueries
import com.roxana.recipeapp.data.recipe.DbCategoryType
import com.roxana.recipeapp.data.recipe.DbTemperatureType
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class RecipeTest {

    private lateinit var db: Database
    private lateinit var queries: RecipeQueries
    private lateinit var categoryQueries: CategoryForRecipeQueries

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.recipeQueries
        categoryQueries = db.categoryForRecipeQueries
    }

    @Test
    fun returnEmptyList_when_emptyDb() {
        // When
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun returnInsertedOrderedItems_when_insertAndSelect() {
        // Given
        val recipeC = Recipe(2, "Crepe", null, null, null, null, null, null, null, null)
        val recipeD = Recipe(1, "Donut", null, null, null, null, null, null, null, null)

        // When
        db.insertRecipe(recipeD)
        db.insertRecipe(recipeC)
        val output = queries.getAll().executeAsList()

        // Then
        output.first().shouldBeEqualToIgnoringFields(recipeC, Recipe::id)
        output.last().shouldBeEqualToIgnoringFields(recipeD, Recipe::id)
    }

    @Test
    fun returnUpdatedItem_when_Update() {
        // Given
        val recipe = Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeUpdated = Recipe(1, "Donut", "path", 1, 2, 3, 4, 5, 6, DbTemperatureType.CELSIUS)

        db.insertRecipe(recipe)

        // When
        db.updateRecipe(recipeUpdated)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldContain(recipeUpdated)
        output.shouldNotContain(recipe)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert("Donut", "path", 1, 2, 3, 4, 5, 6, null)
        val initialRecipe =
            queries.getAll().executeAsList().first { it.name == "Donut" }

        // When
        queries.delete(initialRecipe.id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getRecipeSummaryWithCategories_when_getRecipesSummary() {
        // Given
        queries.insert("Donut", "path", 1, 2, 3, 4, 5, 6, null)
        categoryQueries.insert(DbCategoryType.BREAKFAST, null, 1)
        categoryQueries.insert(DbCategoryType.DESSERT, null, 1)
        categoryQueries.insert(null, null, 1)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(3)
    }

    @Test
    fun getRecipeSummary_when_getRecipesSummary_given_noCategories() {
        // Given
        queries.insert("Donut", "path", 1, 2, 3, 4, 5, 6, null)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(1)
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_totalTimeFilter() {
        // Given
        val recipeNullTotalTime = Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeRightTotalTime = Recipe(2, "Cookie", null, null, 3, null, null, null, null, null)
        val recipeSameTotalTime = Recipe(3, "Cookie", null, null, 4, null, null, null, null, null)
        val recipeWrongTotalTime = Recipe(4, "Brownie", null, null, 5, null, null, null, null, null)
        db.insertRecipe(recipeNullTotalTime)
        db.insertRecipe(recipeRightTotalTime)
        db.insertRecipe(recipeSameTotalTime)
        db.insertRecipe(recipeWrongTotalTime)

        // When
        val output = queries.getRecipesSummary(4, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(3)
        output.map { it.id }
            .shouldContainExactly(
                listOf(
                    recipeNullTotalTime.id,
                    recipeRightTotalTime.id,
                    recipeSameTotalTime.id
                )
            )
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_ignoreTotalTimeFilter() {
        // Given
        val recipeNullTotalTime = Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeTotalTime = Recipe(2, "Cookie", null, null, 3, null, null, null, null, null)
        db.insertRecipe(recipeNullTotalTime)
        db.insertRecipe(recipeTotalTime)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(2)
        output.map { it.id }
            .shouldContainExactly(listOf(recipeNullTotalTime.id, recipeTotalTime.id))
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_cookingTimeFilter() {
        // Given
        val recipeNullCookingTime =
            Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeRightCookingTime =
            Recipe(2, "Cookie", null, null, null, null, 6, null, null, null)
        val recipeSameCookingTime = Recipe(3, "Cookie", null, null, null, null, 8, null, null, null)
        val recipeWrongCookingTime =
            Recipe(4, "Brownie", null, null, null, null, 10, null, null, null)
        db.insertRecipe(recipeNullCookingTime)
        db.insertRecipe(recipeRightCookingTime)
        db.insertRecipe(recipeSameCookingTime)
        db.insertRecipe(recipeWrongCookingTime)

        // When
        val output = queries.getRecipesSummary(null, 8, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(3)
        output.map { it.id }
            .shouldContainExactly(
                listOf(
                    recipeNullCookingTime.id,
                    recipeRightCookingTime.id,
                    recipeSameCookingTime.id
                )
            )
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_ignoreCookingTimeFilter() {
        // Given
        val recipeNullCookingTime =
            Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeCookingTime = Recipe(2, "Cookie", null, null, null, null, 4, null, null, null)
        db.insertRecipe(recipeNullCookingTime)
        db.insertRecipe(recipeCookingTime)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(2)
        output.map { it.id }
            .shouldContainExactly(listOf(recipeNullCookingTime.id, recipeCookingTime.id))
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_preparationTimeFilter() {
        // Given
        val recipeNullPreparationTime =
            Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeRightPreparationTime =
            Recipe(2, "Cookie", null, null, null, 3, null, null, null, null)
        val recipeSamePreparationTime =
            Recipe(3, "Cookie", null, null, null, 4, null, null, null, null)
        val recipeWrongPreparationTime =
            Recipe(4, "Brownie", null, null, null, 5, null, null, null, null)
        db.insertRecipe(recipeNullPreparationTime)
        db.insertRecipe(recipeRightPreparationTime)
        db.insertRecipe(recipeSamePreparationTime)
        db.insertRecipe(recipeWrongPreparationTime)

        // When
        val output = queries.getRecipesSummary(null, null, 4, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(3)
        output.map { it.id }
            .shouldContainExactly(
                listOf(
                    recipeNullPreparationTime.id,
                    recipeRightPreparationTime.id,
                    recipeSamePreparationTime.id
                )
            )
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_ignorePreparationTimeFilter() {
        // Given
        val recipeNullPreparationTime =
            Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipePreparationTime = Recipe(2, "Cookie", null, null, null, 3, null, null, null, null)
        db.insertRecipe(recipeNullPreparationTime)
        db.insertRecipe(recipePreparationTime)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(2)
        output.map { it.id }
            .shouldContainExactly(listOf(recipeNullPreparationTime.id, recipePreparationTime.id))
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_categoryFilter() {
        // Given
        val recipeNullCategory = Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeRightCategory = Recipe(2, "Cookie", null, null, null, 3, null, null, null, null)
        val recipeWrongCategory = Recipe(3, "Brownie", null, null, null, 3, null, null, null, null)
        db.insertRecipe(recipeNullCategory)
        db.insertRecipe(recipeRightCategory)
        db.insertRecipe(recipeWrongCategory)
        categoryQueries.insert(DbCategoryType.DESSERT, null, 2)
        categoryQueries.insert(DbCategoryType.LUNCH, null, 3)

        // When
        val output = queries.getRecipesSummary(null, null, null, DbCategoryType.DESSERT, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(1)
        output.map { it.id }.shouldContainExactly(listOf(recipeRightCategory.id))
    }

    @Test
    fun filterRecipeSummaries_when_getRecipesSummary_given_ignoreCategoryFilter() {
        // Given
        val recipeNullCategory = Recipe(1, "Crepe", null, null, null, null, null, null, null, null)
        val recipeCategory = Recipe(2, "Cookie", null, null, null, 3, null, null, null, null)
        db.insertRecipe(recipeNullCategory)
        db.insertRecipe(recipeCategory)
        categoryQueries.insert(DbCategoryType.DESSERT, null, 2)

        // When
        val output = queries.getRecipesSummary(null, null, null, null, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(2)
        output.map { it.id }
            .shouldContainExactlyInAnyOrder(listOf(recipeCategory.id, recipeNullCategory.id))
    }

    @Test
    fun getMaxTotalTime_when_getMaxTotalTime() {
        // Given
        val time1: Short = 1
        val time2: Short = 5
        val time3: Short = 3
        db.insertRecipe(
            recipe1.copy(
                time_total = time1,
                time_cooking = null,
                time_preparation = null,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = time2,
                time_cooking = null,
                time_preparation = null,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = time3,
                time_cooking = null,
                time_preparation = null,
                time_waiting = null
            )
        )

        // When
        val output = queries.getMaxTotalTime().executeAsOne()

        // Then
        output.MAX shouldBe time2
    }

    @Test
    fun getMaxPreparationTime_when_getMaxPreparationTime() {
        // Given
        val time1: Short = 1
        val time2: Short = 5
        val time3: Short = 3
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = null,
                time_preparation = time1,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = null,
                time_preparation = time2,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = null,
                time_preparation = time3,
                time_waiting = null
            )
        )

        // When
        val output = queries.getMaxPreparationTime().executeAsOne()

        // Then
        output.MAX shouldBe time2
    }

    @Test
    fun getMaxCookingTime_when_getMaxCookingTime() {
        // Given
        val time1: Short = 1
        val time2: Short = 5
        val time3: Short = 3
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = time1,
                time_preparation = null,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = time2,
                time_preparation = null,
                time_waiting = null
            )
        )
        db.insertRecipe(
            recipe1.copy(
                time_total = null,
                time_cooking = time3,
                time_preparation = null,
                time_waiting = null
            )
        )

        // When
        val output = queries.getMaxCookingTime().executeAsOne()

        // Then
        output.MAX shouldBe time2
    }
}
