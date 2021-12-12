package com.roxana.recipeapp.add.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.add.EditingState
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun IndexedView(
    editableState: EditingState,
    index: Int,
    placeholder: String,
    onNameChange: (String) -> Unit,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester()
) {
    if (editableState.isEditing) {
        DeletableTextField(
            state = editableState.fieldState,
            onValueChange = { onNameChange(it) },
            placeholder = placeholder,
            leading = { Text(text = "$index") },
            onDelete = { onDelete() },
            focusRequester = focusRequester,
            imeAction = imeAction,
            onImeAction = onImeAction,
            modifier = modifier
        )
    } else {
        IndexedText(
            text = editableState.fieldState.text,
            index = index,
            modifier = modifier.clickable { onSelect() }
        )
    }
}

@Composable
fun IndexedText(
    text: String,
    index: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(48.dp, 48.dp)
    ) {
        Text(
            text = "$index",
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f)
        )
        Icon(
            painterResource(R.drawable.ic_edit),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.all_edit),
            modifier = Modifier
                .padding(start = 6.dp)
                .padding(vertical = 8.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun InstructionTextPreviewLight() {
    RecipeTheme {
        IndexedText("Flour", 1)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun InstructionTextPreviewDark() {
    RecipeTheme {
        IndexedText("Flour", 1)
    }
}
