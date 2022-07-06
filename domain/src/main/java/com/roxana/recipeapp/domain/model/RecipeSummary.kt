package com.roxana.recipeapp.domain.model

data class RecipeSummary(
    val id: Int,
    val name: String,
    val photoPath: String?,
    val categories: List<CategoryType>
)
