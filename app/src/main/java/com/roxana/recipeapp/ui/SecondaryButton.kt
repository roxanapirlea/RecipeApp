package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
        modifier = modifier,
        content = content
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun SecondaryButtonPreviewLight() {
    RecipeTheme {
        SecondaryButton { Text("Button") }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun SecondaryButtonPreviewDark() {
    RecipeTheme {
        SecondaryButton { Text("Button") }
    }
}
