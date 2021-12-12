package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun CategoryChip(
    text: String,
    modifier: Modifier = Modifier,
    isActivated: Boolean = true,
    onClick: () -> Unit = {}
) {
    val contentAlpha = if (isActivated) ContentAlpha.high else ContentAlpha.disabled
    val borderColor =
        if (isActivated)
            MaterialTheme.colors.secondary
        else
            MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled)
    val background =
        if (isActivated)
            MaterialTheme.colors.surface
        else
            MaterialTheme.colors.background

    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        color = background,
        modifier = modifier
            .semantics { this.selected = isActivated }
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun CategoryUnactivatedPreviewLight() {
    RecipeTheme {
        CategoryChip("Breakfast", isActivated = false)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun CategoryActivatedPreviewLight() {
    RecipeTheme {
        CategoryChip("Breakfast", isActivated = true)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun CategoryUnactivatedPreviewDark() {
    RecipeTheme {
        CategoryChip("Breakfast", isActivated = false)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun CategoryActivatedPreviewDark() {
    RecipeTheme {
        CategoryChip("Breakfast", isActivated = true)
    }
}
