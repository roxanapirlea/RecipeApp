package com.roxana.recipeapp.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow<HomeViewState>(HomeViewState.Empty)
    val state: StateFlow<HomeViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()
}
