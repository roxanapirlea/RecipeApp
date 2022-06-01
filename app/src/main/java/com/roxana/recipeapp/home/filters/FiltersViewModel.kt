package com.roxana.recipeapp.home.filters

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.home.filters.GetMaxTimesUseCase
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getMaxTimesUseCase: GetMaxTimesUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(FiltersViewState())
    val state: StateFlow<FiltersViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val categories = getCategoriesUseCase(null).getOrElse { emptyList() }
            _state.update { state -> state.copy(categories = categories.map { it.toUiModel() }) }
            getMaxTimesUseCase(null)
                .collect { result ->
                    result.onSuccess {
                        _state.update { state ->
                            state.copy(
                                maxTotalTime = it.maxTotal,
                                maxCookingTime = it.maxCooking,
                                maxPreparationTime = it.maxPreparation,
                                selectedTotalTime = it.maxTotal,
                                selectedCookingTime = it.maxCooking,
                                selectedPreparationTime = it.maxPreparation,
                            )
                        }
                    }
                }
        }
    }

    fun onTimeTotalSelected(time: Int) {
        _state.update { it.copy(selectedTotalTime = time) }
    }

    fun onTimeCookingSelected(time: Int) {
        _state.update { it.copy(selectedCookingTime = time) }
    }

    fun onTimePreparationSelected(time: Int) {
        _state.update { it.copy(selectedPreparationTime = time) }
    }

    fun onCategoryClicked(category: UiCategoryType) {
        _state.update {
            val newSelection = if (it.selectedCategory == category) null else category
            it.copy(selectedCategory = newSelection)
        }
    }

    fun onApply() {
        TODO()
    }

    fun onReset() {
        TODO()
    }
}
