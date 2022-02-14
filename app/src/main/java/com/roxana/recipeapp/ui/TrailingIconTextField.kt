package com.roxana.recipeapp.ui

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun TrailingIconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String? = null,
    label: String? = null,
    leading: @Composable (() -> Unit)? = null,
    @DrawableRes trailingIcon: Int = R.drawable.ic_cross,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    onTrailingIconClicked: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        RecipeTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            label = label,
            isError = isError,
            leading = leading,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onImeAction = onImeAction,
            textStyle = textStyle,
            modifier = modifier
                .defaultMinSize(0.dp, 0.dp)
                .weight(3f)
                .focusRequester(focusRequester)
        )
        Icon(
            painterResource(trailingIcon),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_delete),
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable { onTrailingIconClicked() }
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun TrailingIconTextFieldEmptyPreviewLight() {
    RecipeTheme {
        TrailingIconTextField(
            value = "",
            onValueChange = {},
            placeholder = "Placeholder"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun TrailingIconTextFieldEmptyPreviewDark() {
    RecipeTheme {
        TrailingIconTextField(
            value = "",
            onValueChange = {},
            placeholder = "Placeholder"
        )
    }
}
