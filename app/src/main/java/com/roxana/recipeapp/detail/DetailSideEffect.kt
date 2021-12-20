package com.roxana.recipeapp.detail

sealed class DetailSideEffect

object FetchingError : DetailSideEffect()
