package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun TemperatureTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.add_recipe_temperature_hint),
                style = MaterialTheme.typography.body1
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_temperature),
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = MaterialTheme.typography.body1,
        colors = unlinedTextFiledColors(),
        shape = RectangleShape,
        modifier = modifier.defaultMinSize(0.dp, 0.dp)
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun TemperatureTextFieldEmptyPreviewLight() {
    RecipeTheme {
        TemperatureTextField(value = "", onValueChange = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun TemperatureTextFieldFilledPreviewLight() {
    RecipeTheme {
        TemperatureTextField(value = "1", onValueChange = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun TemperatureTextFieldEmptyPreviewDark() {
    RecipeTheme {
        TemperatureTextField(value = "", onValueChange = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun TemperatureTextFieldFilledPreviewDark() {
    RecipeTheme {
        TemperatureTextField(value = "2", onValueChange = {})
    }
}
