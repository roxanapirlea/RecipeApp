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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roxana.recipeapp.R
import com.roxana.recipeapp.ui.CloseIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditRecipeBackdrop(
    recipeAlreadyExists: Boolean,
    selectedPage: PageType,
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
    onSelectPage: (PageType) -> Unit = {},
    onClose: () -> Unit = {},
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
                if (scaffoldState.isConcealed) {
                    IconButton(
                        onClick = { scope.launch { scaffoldState.reveal() } }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_expand),
                            contentDescription = stringResource(R.string.edit_recipe_show_steps),
                            modifier = modifier,
                        )
                    }
                } else {
                    IconButton(onClick = onClose) {
                        CloseIcon(Modifier.padding(horizontal = 12.dp))
                    }
                }

                BackdropAppBar(recipeAlreadyExists) {
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
fun BackdropAppBar(recipeAlreadyExists: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(BackdropScaffoldDefaults.PeekHeight)
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = if (recipeAlreadyExists)
                stringResource(R.string.edit_title_existing_recipe)
            else
                stringResource(R.string.edit_title_new_recipe),
            style = MaterialTheme.typography.h5
        )
    }
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
