package com.roxana.recipeapp.edit.photocapture

data class PhotoCaptureViewState(
    val photoPath: String? = null,
    val error: Error? = null,
    val shouldNavigateBack: Boolean = false
)

enum class Error {
    CONFIG, CAPTURE
}
