package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.detail.IngredientState
import com.roxana.recipeapp.misc.toFormattedString
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.uimodel.UiQuantityType

@Composable
fun IngredientView(
    ingredient: IngredientState,
    modifier: Modifier = Modifier
) {
    val quantity = ingredient.quantity?.toFormattedString() ?: ""
    val quantityType = stringResource(ingredient.quantityType.textForSelected)
    val formattedIngredient =
        if (ingredient.quantity == null)
            ingredient.name
        else
            stringResource(
                R.string.all_ingredient_placeholders,
                quantity,
                quantityType,
                ingredient.name
            )
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
        IngredientView(IngredientState("Water", 2.0, UiQuantityType.Cup), Modifier.fillMaxWidth())
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
        IngredientView(
            IngredientState("Water", 2.0, UiQuantityType.Cup),
            Modifier.fillMaxWidth()
        )
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
        IngredientView(
            IngredientState("Oranges", 2.0, UiQuantityType.None),
            Modifier.fillMaxWidth()
        )
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
        IngredientView(
            IngredientState("Oranges", 2.0, UiQuantityType.None),
            Modifier.fillMaxWidth()
        )
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
        IngredientView(
            IngredientState("Salt", null, UiQuantityType.None),
            Modifier.fillMaxWidth()
        )
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
        IngredientView(
            IngredientState("Salt", null, UiQuantityType.None),
            Modifier.fillMaxWidth()
        )
    }
}
