package com.roxana.recipeapp.comment

data class AddCommentState(
    val comment: String = "",
    val canSave: Boolean = false,
    val isValidated: Boolean = false
)
