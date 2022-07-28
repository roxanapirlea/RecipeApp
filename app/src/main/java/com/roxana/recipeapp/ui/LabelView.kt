package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun LabelView(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        color = color,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun LabelViewPreviewLight() {
    RecipeTheme {
        LabelView(text = "Label", Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun LabelViewPreviewDark() {
    RecipeTheme {
        LabelView(text = "Label", Modifier.fillMaxWidth())
    }
}
