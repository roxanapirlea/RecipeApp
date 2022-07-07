package com.roxana.recipeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.RoundedStartShape
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun RecipeRandomItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedStartShape,
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(2.dp, MaterialTheme.colors.surface),
        modifier = modifier
            .clip(RoundedStartShape)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(80.dp)) {
                Surface(
                    elevation = 0.dp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, MaterialTheme.colors.primary, CircleShape)
                ) {
                    Icon(
                        painterResource(R.drawable.ic_random),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.Center),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            Text(
                text = stringResource(R.string.home_random_recipe),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Light"
)
@Composable
fun RecipeRandomItemPreviewLight() {
    RecipeTheme {
        RecipeRandomItem(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
@Composable
fun RecipeRandomItemPreviewDark() {
    RecipeTheme {
        RecipeRandomItem(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )
    }
}
