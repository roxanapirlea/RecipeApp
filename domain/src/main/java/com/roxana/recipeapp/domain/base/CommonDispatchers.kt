package com.roxana.recipeapp.domain.base

import kotlinx.coroutines.CoroutineDispatcher

data class CommonDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher
)