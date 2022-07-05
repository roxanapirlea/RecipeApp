package com.roxana.recipeapp.permission.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun CameraPermissionExplanation(
    text: String,
    modifier: Modifier = Modifier,
    onRequest: () -> Unit = {}
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 16.dp))
        FlatSecondaryButton(onClick = onRequest) {
            Text(stringResource(R.string.all_request_permission))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraPermissionExplanationPreview() {
    RecipeTheme {
        CameraPermissionExplanation(
            stringResource(R.string.edit_recipe_add_photo_permission),
            Modifier.padding(8.dp)
        )
    }
}
