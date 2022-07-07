package com.roxana.recipeapp.edit.photocapture

data class PhotoCaptureViewState(
    val photoPath: String? = null,
    val isConfigError: Boolean = false,
    val shouldNavigateBack: Boolean = false
)
