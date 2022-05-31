package com.roxana.recipeapp.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun unlinedTextFiledColors() = TextFieldDefaults.textFieldColors(
    textColor = MaterialTheme.colors.onBackground,
    backgroundColor = Color.Transparent,
    leadingIconColor = MaterialTheme.colors.primary,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)

@Composable
fun secondaryOutlineTextFiledColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = MaterialTheme.colors.secondary,
    cursorColor = MaterialTheme.colors.secondary,
)
