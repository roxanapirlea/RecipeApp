package com.roxana.recipeapp.detail.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.CardTitleDetail
import com.roxana.recipeapp.uimodel.UiTemperature

@Composable
fun TemperatureView(
    temperature: Short,
    temperatureUnit: UiTemperature?,
    modifier: Modifier = Modifier
) {
    val unit = temperatureUnit?.let { stringResource(it.text) } ?: ""
    CardTitleDetail(
        modifier = modifier,
        leadingIcon = {
            Icon(painterResource(id = R.drawable.ic_temperature), contentDescription = null)
        },
        title = stringResource(R.string.detail_temperature_label),
        detail = stringResource(R.string.detail_temperature_value, temperature, unit)
    )
}
