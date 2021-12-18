package com.roxana.recipeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(R.drawable.empty_logo),
            contentDescription = null,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.weight(1f))
        H5PrimaryText(
            text = stringResource(R.string.home_empty_description_save),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        H5PrimaryText(
            text = stringResource(R.string.home_empty_description_instructions),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        H5PrimaryText(
            text = stringResource(R.string.home_empty_description_quantities),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Composable
fun H5PrimaryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun AddRowPreviewLight() {
    RecipeTheme {
        EmptyView()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun AddRowPreviewDark() {
    RecipeTheme {
        EmptyView()
    }
}
