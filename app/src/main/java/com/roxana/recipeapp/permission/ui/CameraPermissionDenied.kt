package com.roxana.recipeapp.permission.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.Label
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun CameraPermissionDenied(
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
        Label(
            text = stringResource(R.string.all_camera_permission_denied),
            modifier = modifier.padding(bottom = 16.dp)
        )
        FilledTonalButton(onClick = onRequest) {
            Text(stringResource(R.string.all_request_permission))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraPermissionDeniedPreview() {
    RecipeTheme {
        CameraPermissionDenied(Modifier.padding(8.dp))
    }
}
