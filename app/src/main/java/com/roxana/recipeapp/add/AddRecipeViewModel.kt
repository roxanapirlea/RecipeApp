package com.roxana.recipeapp.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddRecipeViewState())
    val state: StateFlow<AddRecipeViewState> = _state.asStateFlow()

    private val eventChannel = Channel<AddRecipeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onTitleChanged(name: String) {
        _state.value = state.value.copy(title = Title(name, name.isNotEmpty()))
    }
}
