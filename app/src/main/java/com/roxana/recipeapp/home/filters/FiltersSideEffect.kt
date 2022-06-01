package com.roxana.recipeapp.home.filters

sealed class FiltersSideEffect

object Reset : FiltersSideEffect()
object Save : FiltersSideEffect()
