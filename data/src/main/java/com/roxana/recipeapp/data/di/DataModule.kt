package com.roxana.recipeapp.data.di

import android.content.Context
import com.roxana.recipeapp.data.Database
import com.roxana.recipeapp.data.RecipeQueries
import com.roxana.recipeapp.domain.RecipeRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideRecipeQueries(@ApplicationContext context: Context): RecipeQueries {
        val driver = AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "recipes.db"
        )
        return Database(driver).recipeQueries
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinds {
    @Binds
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository
}
