package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme
import com.roxana.recipeapp.ui.unlinedTextFiledColors

@Composable
fun ClearableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onDelete: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text(text = placeholder, style = textStyle) },
            leadingIcon = leading,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            singleLine = true,
            textStyle = textStyle,
            colors = unlinedTextFiledColors(),
            shape = RectangleShape,
            modifier = modifier
                .defaultMinSize(0.dp, 0.dp)
                .weight(3f)
        )
        Icon(
            painterResource(R.drawable.ic_cross),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onDelete() }
                .padding(12.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun ClearableTextFieldEmptyPreviewLight() {
    RecipeTheme {
        ClearableTextField(value = "", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun ClearableTextFieldFilledPreviewLight() {
    RecipeTheme {
        ClearableTextField(value = "Crepe", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun ClearableTextFieldEmptyPreviewDark() {
    RecipeTheme {
        ClearableTextField(value = "", onValueChange = {}, placeholder = "Placeholder")
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun ClearableTextFieldFilledPreviewDark() {
    RecipeTheme {
        ClearableTextField(value = "Crepe", onValueChange = {}, placeholder = "Placeholder")
    }
}
