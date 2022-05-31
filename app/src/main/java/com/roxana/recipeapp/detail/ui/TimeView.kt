package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun TimeView(
    timeTotal: Short?,
    timeCooking: Short?,
    timePreparation: Short?,
    timeWaiting: Short?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        timeTotal?.let {
            ItemDetailsView(text = stringResource(R.string.detail_total_time, it))
        }
        timeCooking?.let {
            ItemDetailsView(text = stringResource(R.string.detail_cooking_time, it))
        }
        timePreparation?.let {
            ItemDetailsView(text = stringResource(R.string.detail_preparation_time, it))
        }
        timeWaiting?.let {
            ItemDetailsView(text = stringResource(R.string.detail_waiting_time, it))
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun TimeViewPreviewLight() {
    RecipeTheme {
        TimeView(6, 3, 2, 1, Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun TimeViewPreviewDark() {
    RecipeTheme {
        TimeView(6, 3, 2, 1, Modifier.fillMaxWidth())
    }
}
