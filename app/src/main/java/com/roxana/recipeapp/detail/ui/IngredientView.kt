package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.misc.formatIngredient
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun IngredientView(
    name: String,
    quantity: Double?,
    quantityType: UiQuantityType,
    modifier: Modifier = Modifier
) {
    val formattedIngredient = formatIngredient(name, quantity, quantityType)
    Text(formattedIngredient, color = MaterialTheme.colors.onBackground, modifier = modifier)
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientViewQuantityAndTypePreviewLight() {
    RecipeTheme {
        IngredientView("Water", 2.0, UiQuantityType.Cup, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientViewQuantityAndTypePreviewDark() {
    RecipeTheme {
        IngredientView("Water", 2.0, UiQuantityType.Cup, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientViewQuantityPreviewLight() {
    RecipeTheme {
        IngredientView("Oranges", 2.0, UiQuantityType.None, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientViewQuantityPreviewDark() {
    RecipeTheme {
        IngredientView("Oranges", 2.0, UiQuantityType.None, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun IngredientViewPreviewLight() {
    RecipeTheme {
        IngredientView("Salt", null, UiQuantityType.None, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun IngredientViewPreviewDark() {
    RecipeTheme {
        IngredientView("Salt", null, UiQuantityType.None, Modifier.fillMaxWidth())
    }
}
