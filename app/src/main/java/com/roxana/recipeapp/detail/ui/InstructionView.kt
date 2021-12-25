package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun InstructionView(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text("$index. ", color = MaterialTheme.colors.onBackground)
        Text(text, color = MaterialTheme.colors.onBackground)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun InstructionViewPreviewLight() {
    RecipeTheme {
        InstructionView(1, "Label", Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun InstructionViewPreviewDark() {
    RecipeTheme {
        InstructionView(1, "Label", Modifier.fillMaxWidth())
    }
}
