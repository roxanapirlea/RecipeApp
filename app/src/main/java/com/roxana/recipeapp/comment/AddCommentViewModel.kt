package com.roxana.recipeapp.comment

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.AddCommentNode
import com.roxana.recipeapp.domain.comment.AddCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCommentViewModel @Inject constructor(
    private val addCommentUseCase: AddCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @VisibleForTesting
    val _state = MutableStateFlow(AddCommentState())
    val state: StateFlow<AddCommentState> = _state.asStateFlow()

    fun onChangeComment(comment: String) {
        _state.value = state.value.copy(comment = comment)
    }

    fun onSaveComment() {
        val recipeId = savedStateHandle.get<Int>(AddCommentNode.KEY_ID)!!
        viewModelScope.launch {
            addCommentUseCase(
                AddCommentUseCase.Input(recipeId, state.value.comment)
            ).getOrNull()?.let { _state.update { it.copy(isValidated = true) } }
        }
    }

    fun onValidateDone() {
        _state.update { it.copy(isValidated = false) }
    }
}
