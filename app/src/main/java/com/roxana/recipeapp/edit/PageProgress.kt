package com.roxana.recipeapp.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.theme.RecipeTheme
import kotlinx.coroutines.launch

@Composable
fun PageProgress(
    recipeAlreadyExists: Boolean,
    selected: PageType,
    onSelectPage: (PageType) -> Unit = {}
) {
    val state = rememberLazyListState()
    val pages =
        if (recipeAlreadyExists) pagesForEditRecipe(selected) else pagesForAddRecipe(selected)

    val coroutineScope = rememberCoroutineScope()

    SideEffect {
        coroutineScope.launch {
            state.scrollToItem(pages.getPreviousIndex(selected))
        }
    }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        state = state,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(pages, { _, page -> page.type.ordinal }) { index, page ->
            ProgressPage(page, index + 1) { onSelectPage(page.type) }
            if (page.hasConnectorEnd) Connector()
        }
    }
}

@Composable
fun ProgressPage(page: Page, index: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val context = LocalContext.current.applicationContext
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                val pageName = context.getString(page.name)
                val description = when {
                    page.isCompleted ->
                        context.getString(R.string.edit_recipe_step_completed, index, pageName)
                    page.isSelected ->
                        context.getString(R.string.edit_recipe_step_current, index, pageName)
                    else -> context.getString(R.string.edit_recipe_step_next, index, pageName)
                }
                contentDescription = description
            }
            .padding(4.dp)
    ) {
        if (page.isCompleted)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                )
            }
        else if (page.isSelected)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Text(
                    "$index",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }
        else
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    "$index",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }
        Text(
            stringResource(page.name),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun Connector(modifier: Modifier = Modifier) {
    Divider(
        color = MaterialTheme.colorScheme.onSurface,
        thickness = 1.dp,
        modifier = modifier.width(24.dp)
    )
}

private fun List<Page>.getPreviousIndex(selected: PageType): Int {
    val current = indexOfFirst { it.type == selected }
    return if (current > 0) current - 1 else 0
}

@Preview(showBackground = true)
@Composable
fun EditPageProgressPreview() {
    RecipeTheme {
        PageProgress(recipeAlreadyExists = false, selected = PageType.Photo)
    }
}
