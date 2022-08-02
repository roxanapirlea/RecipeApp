package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.CustomQuantityType
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.Ingredient
import com.roxana.recipeapp.data.IngredientForRecipe
import com.roxana.recipeapp.data.IngredientForRecipeQueries
import com.roxana.recipeapp.data.recipe.DbQuantityType
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.junit.Before
import org.junit.Test

class IngredientForRecipeTest {
    private lateinit var queries: IngredientForRecipeQueries
    private lateinit var db: Database

    private val recipes = listOf(recipe1, recipe2)
    private lateinit var ingredient1: Ingredient
    private lateinit var ingredient2: Ingredient
    private lateinit var quantity1: CustomQuantityType
    private lateinit var quantity2: CustomQuantityType

    @Before
    fun setUp() {
        db = createInMemoryDb()
        queries = db.ingredientForRecipeQueries
        db.insertFakeRecipes(recipes)
        db.ingredientQueries.insert("ingredient1")
        db.ingredientQueries.insert("ingredient2")
        db.customQuantityTypeQueries.insert("quantity1")
        db.customQuantityTypeQueries.insert("quantity2")
        ingredient1 =
            db.ingredientQueries.getAll().executeAsList().first { it.name == "ingredient1" }
        ingredient2 =
            db.ingredientQueries.getAll().executeAsList().first { it.name == "ingredient2" }
        quantity1 =
            db.customQuantityTypeQueries.getAll().executeAsList().first { it.name == "quantity1" }
        quantity2 =
            db.customQuantityTypeQueries.getAll().executeAsList().first { it.name == "quantity2" }
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
        val ingrNoQuantity = IngredientForRecipe(1, null, null, null, ingredient1.id, recipes[0].id)
        val ingrNoQuantityType = IngredientForRecipe(1, 1.0, null, null, ingredient1.id, recipes[0].id)
        val ingrStandardQuantityType =
            IngredientForRecipe(1, 1.0, DbQuantityType.CUP, null, ingredient1.id, recipes[0].id)
        val ingrCustomQuantityType =
            IngredientForRecipe(1, 1.0, null, quantity1.id, ingredient1.id, recipes[0].id)

        // When
        queries.insert(
            ingrNoQuantity.quantity,
            ingrNoQuantity.quantity_name,
            ingrNoQuantity.quantity_type_id,
            ingrNoQuantity.ingredient_id,
            ingrNoQuantity.recipe_id
        )
        queries.insert(
            ingrNoQuantityType.quantity,
            ingrNoQuantityType.quantity_name,
            ingrNoQuantityType.quantity_type_id,
            ingrNoQuantityType.ingredient_id,
            ingrNoQuantityType.recipe_id
        )
        queries.insert(
            ingrStandardQuantityType.quantity,
            ingrStandardQuantityType.quantity_name,
            ingrStandardQuantityType.quantity_type_id,
            ingrStandardQuantityType.ingredient_id,
            ingrStandardQuantityType.recipe_id
        )
        queries.insert(
            ingrCustomQuantityType.quantity,
            ingrCustomQuantityType.quantity_name,
            ingrCustomQuantityType.quantity_type_id,
            ingrCustomQuantityType.ingredient_id,
            ingrCustomQuantityType.recipe_id
        )
        val output = queries.getAll().executeAsList()

        // Then
        output shouldHaveSize 4
        output[0].shouldBeEqualToIgnoringFields(ingrNoQuantity, IngredientForRecipe::id)
        output[1].shouldBeEqualToIgnoringFields(ingrNoQuantityType, IngredientForRecipe::id)
        output[2].shouldBeEqualToIgnoringFields(ingrStandardQuantityType, IngredientForRecipe::id)
        output[3].shouldBeEqualToIgnoringFields(ingrCustomQuantityType, IngredientForRecipe::id)
    }

    @Test
    fun deleteItem_when_delete() {
        // Given
        queries.insert(1.0, DbQuantityType.CUP, null, ingredient1.id, recipes[0].id)

        // When
        queries.deleteByRecipeId(recipes[0].id)
        val output = queries.getAll().executeAsList()

        // Then
        output.shouldBeEmpty()
    }

    @Test
    fun getItems_when_getByRecipeId() {
        // Given
        queries.insert(1.0, DbQuantityType.CUP, null, ingredient1.id, recipes[0].id)
        queries.insert(1.0, null, quantity1.id, ingredient1.id, recipes[0].id)
        queries.insert(1.0, DbQuantityType.CUP, null, ingredient1.id, recipes[1].id)

        // When
        val output = queries.getIngredientByRecipeId(recipes[0].id).executeAsList()

        // Then
        output shouldHaveSize 2
    }
}
