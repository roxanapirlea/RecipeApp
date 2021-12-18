package com.roxana.recipeapp.add

sealed class TextFieldState(
    open val text: String
) {
    open val isValid: Boolean = true
}

data class NonEmptyFieldState(
    override val text: String = ""
) : TextFieldState(text) {
    override val isValid: Boolean = text.isNotEmpty()
}

data class EmptyFieldState(
    override val text: String = ""
) : TextFieldState(text)

data class IntFieldState(
    override val text: String = "",
    val value: Int? = null
) : TextFieldState(text) {
    override val isValid: Boolean = text.isEmpty() || text.toIntOrNull() != null
}

data class ShortFieldState(
    override val text: String = "",
    val value: Short? = null
) : TextFieldState(text) {
    override val isValid: Boolean = text.isEmpty() || text.toShortOrNull() != null
}

data class DoubleFieldState(
    override val text: String = "",
    val value: Double? = null
) : TextFieldState(text) {
    override val isValid: Boolean = text.isEmpty() || text.toDoubleOrNull() != null
}
