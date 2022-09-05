package com.roxana.recipeapp.edit.title.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.edit.title.EditRecipeTitleViewState
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.basecomponents.RecipeOutlinedTextField

@Composable
fun EditRecipeTitleView(
    state: EditRecipeTitleViewState,
    focusRequester: FocusRequester,
    onTitleChanged: (String) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(3f))
        Label(
            text = stringResource(R.string.edit_recipe_title_label),
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Spacer(modifier = Modifier.weight(0.5f))
        RecipeOutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            placeholder = stringResource(R.string.edit_recipe_title_hint),
            imeAction = ImeAction.Done,
            onImeAction = onValidate,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.weight(6f))
    }
}
