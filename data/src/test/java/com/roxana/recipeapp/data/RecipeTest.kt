package com.roxana.recipeapp.data

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class RecipeTest {

    private lateinit var queries: RecipeQueries
    private lateinit var categoryQueries: CategoryForRecipeQueries

    @Before
    fun setUp() {
        val db = createInMemoryDb()
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
        queries.insert(
            name = recipeD.name,
            photo_path = recipeD.photo_path,
            portions = recipeD.portions,
            time_total = recipeD.time_total,
            time_cooking = recipeD.time_cooking,
            time_preparation = recipeD.time_preparation,
            time_waiting = recipeD.time_waiting,
            temperature = recipeD.temperature,
            temperature_type = recipeD.temperature_type
        )
        queries.insert(
            name = recipeC.name,
            photo_path = recipeC.photo_path,
            portions = recipeC.portions,
            time_total = recipeC.time_total,
            time_cooking = recipeC.time_cooking,
            time_preparation = recipeC.time_preparation,
            time_waiting = recipeC.time_waiting,
            temperature = recipeC.temperature,
            temperature_type = recipeC.temperature_type
        )
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
        queries.insert(
            name = recipe.name,
            photo_path = recipe.photo_path,
            portions = recipe.portions,
            time_total = recipe.time_total,
            time_cooking = recipe.time_cooking,
            time_preparation = recipe.time_preparation,
            time_waiting = recipe.time_waiting,
            temperature = recipe.temperature,
            temperature_type = recipe.temperature_type
        )

        // When
        queries.update(
            name = recipeUpdated.name,
            photo_path = recipeUpdated.photo_path,
            portions = recipeUpdated.portions,
            time_total = recipeUpdated.time_total,
            time_cooking = recipeUpdated.time_cooking,
            time_preparation = recipeUpdated.time_preparation,
            time_waiting = recipeUpdated.time_waiting,
            temperature = recipeUpdated.temperature,
            temperature_type = recipeUpdated.temperature_type,
            id = recipeUpdated.id
        )
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
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(3)
    }

    @Test
    fun getRecipeSummary_when_getRecipesSummary_given_noCategories() {
        // Given
        queries.insert("Donut", "path", 1, 2, 3, 4, 5, 6, null)

        // When
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
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
        with(recipeNullTotalTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeRightTotalTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeSameTotalTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeWrongTotalTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(4, false, null, true, null, true, null, true, "")
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
        with(recipeNullTotalTime) {
            queries.insert(
                name,
                photo_path,
                portions,
                time_total,
                time_preparation,
                time_cooking,
                time_waiting,
                temperature,
                temperature_type
            )
        }
        with(recipeTotalTime) {
            queries.insert(
                name,
                photo_path,
                portions,
                time_total,
                time_preparation,
                time_cooking,
                time_waiting,
                temperature,
                temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
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
        with(recipeNullCookingTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeRightCookingTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeSameCookingTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeWrongCookingTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(null, true, 8, false, null, true, null, true, "")
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
        with(recipeNullCookingTime) {
            queries.insert(
                name,
                photo_path,
                portions,
                time_total,
                time_preparation,
                time_cooking,
                time_waiting,
                temperature,
                temperature_type
            )
        }
        with(recipeCookingTime) {
            queries.insert(
                name,
                photo_path,
                portions,
                time_total,
                time_preparation,
                time_cooking,
                time_waiting,
                temperature,
                temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
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
        with(recipeNullPreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeRightPreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeSamePreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeWrongPreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(null, true, null, true, 4, false, null, true, "")
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
        with(recipeNullPreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipePreparationTime) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }

        // When
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
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
        with(recipeNullCategory) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeRightCategory) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeWrongCategory) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        categoryQueries.insert(DbCategoryType.DESSERT, null, 2)
        categoryQueries.insert(DbCategoryType.LUNCH, null, 3)

        // When
        val output = queries.getRecipesSummary(
            null,
            true,
            null,
            true,
            null,
            true,
            DbCategoryType.DESSERT,
            false,
            ""
        )
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
        with(recipeNullCategory) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        with(recipeCategory) {
            queries.insert(
                name = name,
                photo_path = photo_path,
                portions = portions,
                time_total = time_total,
                time_cooking = time_cooking,
                time_preparation = time_preparation,
                time_waiting = time_waiting,
                temperature = temperature,
                temperature_type = temperature_type
            )
        }
        categoryQueries.insert(DbCategoryType.DESSERT, null, 2)

        // When
        val output = queries.getRecipesSummary(null, true, null, true, null, true, null, true, "")
            .executeAsList()

        // Then
        output.shouldHaveSize(2)
        output.map { it.id }
            .shouldContainExactlyInAnyOrder(listOf(recipeCategory.id, recipeNullCategory.id))
    }
}
