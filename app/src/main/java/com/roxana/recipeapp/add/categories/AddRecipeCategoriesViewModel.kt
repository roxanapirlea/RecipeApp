package com.roxana.recipeapp.add.categories

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.domain.addrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.addrecipe.GetCategoriesUseCase
import com.roxana.recipeapp.domain.addrecipe.SetCategoriesUseCase
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeCategoriesViewModel @Inject constructor(
    private val getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val setCategoriesUseCase: SetCategoriesUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(AddRecipeCategoriesViewState())
    val state: StateFlow<AddRecipeCategoriesViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<AddRecipeCategoriesSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val selected = getCategoriesUseCase(null).first()
                .getOrElse { emptySet() }
            val availableCategories = getAvailableCategoriesUseCase(null)
                .getOrElse { emptyList() }
            val categories =
                availableCategories.map { CategoryState(it.toUiModel(), it in selected) }
            _state.value = AddRecipeCategoriesViewState(categories)
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
                { sideEffectChannel.send(Forward) },
                { sideEffectChannel.send(Forward) }
            )
        }
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setCategoriesUseCase(getSelectedCategories()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setCategoriesUseCase(getSelectedCategories()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getSelectedCategories() = state.value.categories
        .filter { it.isSelected }
        .map { it.categoryType.toDomainModel() }
        .toSet()
}
