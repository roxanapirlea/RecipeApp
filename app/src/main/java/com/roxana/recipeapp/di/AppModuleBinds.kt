package com.roxana.recipeapp.di

import com.roxana.recipeapp.common.TimberLogger
import com.roxana.recipeapp.domain.common.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {
    @Binds
    abstract fun bindLogger(impl: TimberLogger): Logger
}