package com.roxana.recipeapp.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.addrecipe.GetAvailableCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAvailableCategoriesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddRecipeViewState())
    val state: StateFlow<AddRecipeViewState> = _state.asStateFlow()

    private val eventChannel = Channel<AddRecipeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getCategoriesUseCase.invoke(null).fold(
                { availableCategories ->
                    val categories = availableCategories.map { Category(it, false) }
                    _state.emit(state.value.copy(categories = categories))
                },
                { eventChannel.send(ShowCategoryError) }
            )
        }
    }

    fun onTitleChanged(name: String) {
        _state.value = state.value.copy(title = Title(name, name.isNotEmpty()))
    }

    fun onCategoryClicked(type: CategoryType) {
        val categories = state.value.categories.map {
            if (it.type == type) it.copy(isSelected = !it.isSelected)
            else it
        }
        _state.value = state.value.copy(categories = categories)
    }

    fun onPortionsChanged(portions: String) {
        _state.value = state.value.copy(
            portions = PortionsState(
                portions.toShortOrNull(),
                portions,
                portions.isShort()
            )
        )
    }

    private fun String.isShort(): Boolean {
        if (isEmpty()) return true
        toShortOrNull()?.let { return true } ?: return false
    }
}
