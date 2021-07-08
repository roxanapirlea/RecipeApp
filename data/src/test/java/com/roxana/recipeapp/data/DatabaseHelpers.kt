package com.roxana.recipeapp.data

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
        )
    )
}
