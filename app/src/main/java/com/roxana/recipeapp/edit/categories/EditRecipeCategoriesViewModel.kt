package com.roxana.recipeapp.edit.categories

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.editrecipe.GetCategoriesUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetCategoriesUseCase
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeCategoriesViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val setCategoriesUseCase: SetCategoriesUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeCategoriesViewState())
    val state: StateFlow<EditRecipeCategoriesViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val selected = getCategoriesUseCase(null).first()
                .getOrElse { emptySet() }
            val availableCategories = getAvailableCategoriesUseCase(null)
                .getOrElse { emptyList() }
            val categories =
                availableCategories.map { CategoryState(it.toUiModel(), it in selected) }
            _state.value = EditRecipeCategoriesViewState(categories, isExistingRecipe)
        }
    }

    fun onCategoryClicked(categoryType: UiCategoryType) {
        _state.update { state ->
            val categories = state.categories
                .map {
                    if (it.categoryType == categoryType)
                        it.copy(isSelected = !it.isSelected)
                    else it
                }
            state.copy(categories = categories)
        }
    }

    fun onValidate() {
        viewModelScope.launch {
            setCategoriesUseCase(getSelectedCategories()).fold(
                { sendForwardEvent() },
                { sendForwardEvent() }
            )
        }
    }

    private fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            _state.update { it.copy(navigation = Navigation.ForwardEditing) }
        else
            _state.update { it.copy(navigation = Navigation.ForwardCreation) }
    }

    fun onResetAndClose() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            resetRecipeUseCase(null).fold(
                { _state.update { it.copy(navigation = Navigation.Close) } },
                { _state.update { it.copy(navigation = Navigation.Close) } }
            )
        }
    }

    fun onSaveAndClose() {
        _state.update { it.copy(showSaveDialog = false) }
        viewModelScope.launch {
            setCategoriesUseCase(getSelectedCategories()).fold(
                { _state.update { it.copy(navigation = Navigation.Close) } },
                { _state.update { it.copy(navigation = Navigation.Close) } }
            )
        }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    fun onCheckShouldClose() {
        _state.update { it.copy(showSaveDialog = true) }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setCategoriesUseCase(getSelectedCategories()).fold(
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } },
                { _state.update { it.copy(navigation = Navigation.ToPage(page)) } }
            )
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
    }

    private fun getSelectedCategories() = state.value.categories
        .filter { it.isSelected }
        .map { it.categoryType.toDomainModel() }
        .toSet()
}
