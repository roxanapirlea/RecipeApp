package com.roxana.recipeapp.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.CardTitleDetail
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            timeTotal?.let {
                CardTitleDetail(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    title = stringResource(R.string.detail_total_time),
                    detail = stringResource(R.string.detail_time, it)
                )
            }
            timeCooking?.let {
                CardTitleDetail(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    title = stringResource(R.string.detail_cooking_time),
                    detail = stringResource(R.string.detail_time, it)
                )
            }
            timePreparation?.let {
                CardTitleDetail(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    title = stringResource(R.string.detail_preparation_time),
                    detail = stringResource(R.string.detail_time, it)
                )
            }
            timeWaiting?.let {
                CardTitleDetail(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    title = stringResource(R.string.detail_waiting_time),
                    detail = stringResource(R.string.detail_time, it)
                )
            }
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
