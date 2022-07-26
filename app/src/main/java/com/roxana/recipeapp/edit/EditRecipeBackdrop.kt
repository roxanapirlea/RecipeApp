package com.roxana.recipeapp.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.BackIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeBackdrop(
    recipeAlreadyExists: Boolean,
    selectedPage: PageType,
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    onSelectPage: (PageType) -> Unit = {},
    onNavIcon: () -> Unit = {},
    navIcon: @Composable () -> Unit = { BackIcon() },
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
                IconButton(
                    onClick = onNavIcon,
                    content = navIcon
                )

                BackdropAppBar(
                    recipeAlreadyExists,
                    selectedPage,
                    scaffoldState.isRevealed,
                    scaffoldState.isConcealed
                ) {
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
                recipeAlreadyExists,
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
fun BackdropAppBar(
    recipeAlreadyExists: Boolean,
    selected: PageType,
    isBackdropRevealed: Boolean,
    isBackdropConcealed: Boolean,
    onClick: () -> Unit
) {
    val state = rememberLazyListState()
    val pages =
        if (recipeAlreadyExists) pagesForEditRecipe(selected) else pagesForAddRecipe(selected)
    LaunchedEffect(pages) {
        state.scrollToItem(pages.indexOfFirst { it.isSelected })
    }

    if (isBackdropRevealed)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BackdropScaffoldDefaults.PeekHeight)
                .clickable { onClick() }
        ) {
            Text(
                stringResource(
                    if (recipeAlreadyExists) R.string.edit_title_existing_recipe
                    else R.string.edit_title_new_recipe
                ),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    if (isBackdropConcealed)
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(BackdropScaffoldDefaults.PeekHeight)
                .clickable { onClick() }
        ) {
            items(pages, { it.type.ordinal }) { page ->
                if (page.hasDivider)
                    VerticalDivider(
                        Modifier
                            .height(BackdropScaffoldDefaults.PeekHeight)
                            .padding(vertical = 4.dp)
                    )
                ConcealedPage(page, Modifier.padding(horizontal = 16.dp))
            }
        }
}

@Composable
fun ConcealedPage(page: Page, modifier: Modifier = Modifier) {
    val textWeight = if (page.isSelected) FontWeight.Black else FontWeight.Normal
    val textDecoration = if (page.isSelected) TextDecoration.Underline else TextDecoration.None
    val alpha = if (page.isSelected) ContentAlpha.high else ContentAlpha.disabled
    CompositionLocalProvider(LocalContentAlpha provides alpha) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Icon(
                painterResource(page.icon),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                stringResource(page.name),
                Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.subtitle1,
                textDecoration = textDecoration,
                fontWeight = textWeight
            )
        }
    }
}

@Composable
fun VerticalDivider(modifier: Modifier) {
    Box(
        modifier
            .width(1.dp)
            .background(color = MaterialTheme.colors.onPrimary)
    )
}

@Composable
fun BackdropBackLayer(
    recipeAlreadyExists: Boolean,
    selected: PageType,
    onSelectPage: (PageType) -> Unit = {}
) {
    Column(Modifier.padding(16.dp)) {
        if (recipeAlreadyExists)
            pagesForEditRecipe(selected).forEach { page ->
                BackdropPage(page) { onSelectPage(page.type) }
            }
        else
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
