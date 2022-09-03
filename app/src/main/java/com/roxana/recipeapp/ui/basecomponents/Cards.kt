package com.roxana.recipeapp.ui.basecomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardEndImage(
    modifier: Modifier = Modifier,
    endImage: (@Composable () -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable () -> Unit
) {
    Card(modifier = modifier, colors = colors) {
        CardEndImageContent(endImage, content)
    }
}

@Composable
fun ElevatedCardEndImage(
    modifier: Modifier = Modifier,
    endImage: (@Composable () -> Unit)? = null,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    content: @Composable () -> Unit
) {
    ElevatedCard(modifier = modifier, colors = colors) {
        CardEndImageContent(endImage, content)
    }
}

@Composable
fun OutlineCardEndImage(
    modifier: Modifier = Modifier,
    endImage: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    OutlinedCard(modifier = modifier) {
        CardEndImageContent(endImage, content)
    }
}

@Composable
private fun CardEndImageContent(
    endImage: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(start = 24.dp)
                .weight(1f)
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .size(90.dp)
        ) {
            if (endImage != null)
                endImage()
            else
                Spacer(
                    modifier = Modifier
                        .size(90.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
        }
    }
}

@Composable
fun CardTitleDetail(
    modifier: Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    detail: String
) {
    ElevatedCard(modifier) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.let { Box(modifier = Modifier.padding(end = 8.dp)) { it() } }
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium)
                Text(detail, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
