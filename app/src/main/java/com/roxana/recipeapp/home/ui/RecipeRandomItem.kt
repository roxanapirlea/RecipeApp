package com.roxana.recipeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.basecomponents.ElevatedCardEndImage
import com.roxana.recipeapp.ui.theme.RecipeTheme

@Composable
fun RecipeRandomItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ElevatedCardEndImage(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        endImage = {
            Icon(
                painterResource(R.drawable.ic_random),
                contentDescription = null,
                modifier = Modifier
                    .padding(26.dp)
                    .size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        Text(
            text = stringResource(R.string.home_random_recipe),
            style = MaterialTheme.typography.titleMedium,
        )
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
        RecipeRandomItem(modifier = Modifier.padding(16.dp))
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
        RecipeRandomItem(modifier = Modifier.padding(16.dp))
    }
}
