package com.roxana.recipeapp.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.home.GetRecipesSummaryUseCase
import com.roxana.recipeapp.uimodel.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesSummaryUseCase: GetRecipesSummaryUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val state: StateFlow<HomeViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getRecipesSummaryUseCase(null)
                .collect { result ->
                    result.fold(
                        { recipesSummary ->
                            if (recipesSummary.isEmpty())
                                _state.value = HomeViewState.Empty
                            else {
                                val recipes =
                                    recipesSummary.map { summary ->
                                        RecipeState(
                                            summary.id,
                                            summary.name,
                                            summary.categories.map { it.toUiModel() }
                                        )
                                    }
                                _state.value = HomeViewState.Content(recipes)
                            }
                        },
                        {
                            sideEffectChannel.send(ItemsFetchingError)
                        }
                    )
                }
        }
    }
}
