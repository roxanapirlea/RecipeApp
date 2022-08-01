package com.roxana.recipeapp.data.queries

import com.roxana.recipeapp.data.CategoryForRecipe
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.IngredientForRecipe
import com.roxana.recipeapp.data.Recipe
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

fun createInMemoryDb(): Database {
    val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        Database.Schema.create(this)
    }
    return Database(
        inMemorySqlDriver,
        CategoryForRecipeAdapter = CategoryForRecipe.Adapter(nameAdapter = EnumColumnAdapter()),
        IngredientForRecipeAdapter = IngredientForRecipe.Adapter(
            quantity_nameAdapter = EnumColumnAdapter()
        ),
        RecipeAdapter = Recipe.Adapter(temperature_typeAdapter = EnumColumnAdapter())
    )
}

fun Database.insertFakeRecipes(recipes: List<Recipe>) {
    recipes.forEach { recipe ->
        recipeQueries.insert(
            recipe.name,
            recipe.photo_path,
            recipe.portions,
            recipe.time_total,
            recipe.time_preparation,
            recipe.time_cooking,
            recipe.time_waiting,
            recipe.temperature,
            recipe.temperature_type
        )
    }
}

fun Database.insertRecipe(recipe: Recipe) {
    recipeQueries.insert(
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
}

fun Database.updateRecipe(recipe: Recipe) {
    recipeQueries.update(
        name = recipe.name,
        photo_path = recipe.photo_path,
        portions = recipe.portions,
        time_total = recipe.time_total,
        time_cooking = recipe.time_cooking,
        time_preparation = recipe.time_preparation,
        time_waiting = recipe.time_waiting,
        temperature = recipe.temperature,
        temperature_type = recipe.temperature_type,
        id = recipe.id
    )
}
