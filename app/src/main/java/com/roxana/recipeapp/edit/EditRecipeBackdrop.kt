package com.roxana.recipeapp.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldDefaults
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeBackdrop(
    selectedPage: PageType,
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    backIcon: ImageVector,
    backContentDescription: String,
    onSelectPage: (PageType) -> Unit = {},
    onBack: () -> Unit = {},
    frontLayerContent: @Composable () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        frontLayerBackgroundColor = MaterialTheme.colors.background,
        frontLayerContentColor = MaterialTheme.colors.onBackground,
        appBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(BackdropScaffoldDefaults.PeekHeight)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        backIcon,
                        contentDescription = backContentDescription,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                BackdropAppBar {
                    scope.launch {
                        if (scaffoldState.isRevealed)
                            scaffoldState.conceal()
                        else
                            scaffoldState.reveal()
                    }
                }
            }
        },
        backLayerContent = {
            BackdropBackLayer(
                selectedPage,
                onSelectPage = {
                    onSelectPage(it)
                    scope.launch {
                        scaffoldState.conceal()
                    }
                }
            )
        },
        frontLayerContent = frontLayerContent
    )
}

@Composable
fun BackdropAppBar(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(BackdropScaffoldDefaults.PeekHeight)
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Text(
            stringResource(R.string.edit_title),
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun BackdropBackLayer(
    selected: PageType,
    onSelectPage: (PageType) -> Unit = {}
) {
    Column(Modifier.padding(16.dp)) {
        pagesForAddRecipe(selected).forEach { page ->
            BackdropPage(page) { onSelectPage(page.type) }
        }
    }
}

@Composable
fun BackdropPage(
    page: Page,
    onSelect: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Text(
            stringResource(page.name),
            Modifier
                .padding(vertical = 8.dp)
                .alignByBaseline(),
            style = MaterialTheme.typography.h5
        )
        if (page.isSelected)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    stringResource(R.string.edit_recipe_current_page),
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .alignByBaseline(),
                    style = MaterialTheme.typography.subtitle1
                )
            }
    }
}
