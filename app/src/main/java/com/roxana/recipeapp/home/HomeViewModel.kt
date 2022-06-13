package com.roxana.recipeapp.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetAvailableCategoriesUseCase
import com.roxana.recipeapp.domain.home.GetRecipesSummaryUseCase
import com.roxana.recipeapp.domain.home.filters.GetMaxTimesUseCase
import com.roxana.recipeapp.domain.model.RecipeSummary
import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.toDomainModel
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesSummaryUseCase: GetRecipesSummaryUseCase,
    private val getCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getMaxTimesUseCase: GetMaxTimesUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val state: StateFlow<HomeViewState> = _state.asStateFlow()

    private val _filters = MutableStateFlow(FiltersSelection())

    private val sideEffectChannel = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                _filters.flatMapLatest { selectedFilters ->
                    getRecipesSummaryUseCase(
                        GetRecipesSummaryUseCase.Input(
                            query = selectedFilters.query,
                            totalTime = selectedFilters.totalTime?.toShort(),
                            cookingTime = selectedFilters.cookingTime?.toShort(),
                            preparationTime = selectedFilters.preparationTime?.toShort(),
                            category = selectedFilters.category?.toDomainModel()
                        )
                    ).map { result -> result to selectedFilters }
                },
                getMaxTimesUseCase(null)
            ) { (recipesResult, selectedFilters), maxTimesResult ->
                val recipesSummary = recipesResult.getOrElse {
                    sideEffectChannel.send(ItemsFetchingError)
                    emptyList()
                }
                mapContent(recipesSummary, maxTimesResult, selectedFilters)
            }.collect { state ->
                _state.update {
                    if (state is HomeViewState.Content && it is HomeViewState.Content)
                        it.copy(
                            recipes = state.recipes,
                            filtersState = state.filtersState,
                            filtersSelectionCount = state.filtersSelectionCount
                        )
                    else state
                }
            }
        }
    }

    private suspend fun mapContent(
        recipesSummary: List<RecipeSummary>,
        maxTimesResult: Result<GetMaxTimesUseCase.Output>,
        selectedFilters: FiltersSelection
    ): HomeViewState {
        val recipes = recipesSummary.map { it.toState() }
        val categories = getCategoriesUseCase(null).getOrElse { emptyList() }
        val query = selectedFilters.query

        val filtersState = maxTimesResult.getOrNull()?.let {
            FiltersState(
                categories = categories.map { category -> category.toUiModel() },
                maxTotalTime = it.maxTotal?.toInt(),
                maxCookingTime = it.maxCooking?.toInt(),
                maxPreparationTime = it.maxPreparation?.toInt(),
                selectedCategory = selectedFilters.category,
                selectedTotalTime = selectedFilters.totalTime ?: it.maxTotal?.toInt(),
                selectedCookingTime = selectedFilters.cookingTime ?: it.maxCooking?.toInt(),
                selectedPreparationTime =
                selectedFilters.preparationTime ?: it.maxPreparation?.toInt(),
                query = query
            )
        } ?: FiltersState()

        val filtersSelectionCount: Int = computeFilterCount(filtersState)

        return if (recipes.isEmpty() && filtersSelectionCount == 0 && query.isBlank())
            HomeViewState.Empty
        else
            HomeViewState.Content(recipes, false, filtersState, filtersSelectionCount)
    }

    private fun RecipeSummary.toState() = RecipeState(id, name, categories.map { it.toUiModel() })

    private fun computeFilterCount(filters: FiltersState): Int {
        var count = 0
        if (filters.selectedTotalTime.orZero() < filters.maxTotalTime.orZero())
            count++
        if (filters.selectedCookingTime.orZero() < filters.maxCookingTime.orZero())
            count++
        if (filters.selectedPreparationTime.orZero() < filters.maxPreparationTime.orZero())
            count++
        if (filters.selectedCategory != null) count++
        return count
    }

    private fun Int?.orZero(): Int = this ?: 0

    fun onFiltersClicked() {
        _state.update {
            when (it) {
                is HomeViewState.Content -> it.copy(showFilters = true)
                else -> it
            }
        }
    }

    fun onResetFiltersClicked() {
        _state.update {
            when (it) {
                is HomeViewState.Content -> it.copy(showFilters = false)
                else -> it
            }
        }
        _filters.value = FiltersSelection()
    }

    fun onCloseFiltersClicked() {
        _state.update {
            when (it) {
                is HomeViewState.Content -> it.copy(showFilters = false)
                else -> it
            }
        }
    }

    fun onTotalTimeSelected(time: Int) {
        _filters.update { it.copy(totalTime = time) }
    }

    fun onCookingTimeSelected(time: Int) {
        _filters.update { it.copy(cookingTime = time) }
    }

    fun onPreparationTimeSelected(time: Int) {
        _filters.update { it.copy(preparationTime = time) }
    }

    fun onCategoryClicked(category: UiCategoryType) {
        _filters.update {
            val newSelection = if (it.category == category) null else category
            it.copy(category = newSelection)
        }
    }

    fun onQueryModified(query: String) {
        _filters.update { it.copy(query = query) }
    }
}
