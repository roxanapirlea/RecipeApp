package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun FlatSecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
        modifier = modifier,
        enabled = isEnabled,
        content = content,
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun FlatSecondaryButtonPreviewLight() {
    RecipeTheme {
        FlatSecondaryButton { Text("Button") }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun FlatSecondaryButtonPreviewDark() {
    RecipeTheme {
        FlatSecondaryButton { Text("Button") }
    }
}
