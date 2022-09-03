package com.roxana.recipeapp.ui.textfield

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun primaryOutlineTextFiledColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = MaterialTheme.colors.primary,
    cursorColor = MaterialTheme.colors.primary,
)

@Composable
fun secondaryOutlineTextFiledColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = MaterialTheme.colors.secondary,
    cursorColor = MaterialTheme.colors.secondary,
)
