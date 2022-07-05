package com.roxana.recipeapp.permission.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.FlatSecondaryButton
import com.roxana.recipeapp.ui.LabelView
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
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_error),
                contentDescription = stringResource(R.string.all_error),
                tint = MaterialTheme.colors.error
            )
            LabelView(
                text = stringResource(R.string.all_camera_permission_denied),
                modifier = modifier
            )
        }
        FlatSecondaryButton(onClick = onRequest) {
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
