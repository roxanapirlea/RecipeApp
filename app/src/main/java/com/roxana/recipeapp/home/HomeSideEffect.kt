package com.roxana.recipeapp.home

sealed class HomeSideEffect

object ItemsFetchingError : HomeSideEffect()
