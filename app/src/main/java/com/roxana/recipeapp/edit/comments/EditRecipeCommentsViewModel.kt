package com.roxana.recipeapp.edit.comments

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.GetCommentsUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.SetCommentsUseCase
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.edit.PageType
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
class EditRecipeCommentsViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val setCommentsUseCase: SetCommentsUseCase
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeCommentsViewState())
    val state: StateFlow<EditRecipeCommentsViewState> = _state.asStateFlow()

    private val sideEffectChannel = Channel<EditRecipeCommentsSideEffect>(Channel.BUFFERED)
    val sideEffectFlow = sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
            val comments = getCommentsUseCase(null).first().getOrElse { emptyList() }
                .map { it.detail }
            _state.update {
                it.copy(
                    comments = comments,
                    isExistingRecipe = isExistingRecipe
                )
            }
        }
    }

    fun onCommentChanged(text: String) {
        _state.update {
            it.copy(editingComment = text)
        }
    }

    fun onSaveComment() {
        _state.update { state ->
            state.copy(
                comments = state.comments + state.editingComment,
                editingComment = ""
            )
        }
    }

    fun onCommentDone() {
        if (state.value.editingComment.isEmpty()) onValidate() else onSaveComment()
    }

    fun onDeleteComment(indexToDelete: Int) {
        _state.update {
            val instructions = it.comments.filterIndexed { index, _ -> index != indexToDelete }
            it.copy(comments = instructions)
        }
    }

    fun onValidate() {
        viewModelScope.launch {
            setCommentsUseCase(getAllComments()).fold(
                { sendForwardEvent() },
                { sendForwardEvent() }
            )
        }
    }

    private suspend fun sendForwardEvent() {
        val isExistingRecipe = state.value.isExistingRecipe
        if (isExistingRecipe)
            sideEffectChannel.send(ForwardForEditing)
        else
            sideEffectChannel.send(ForwardForCreation)
    }

    fun onSaveAndBack() {
        viewModelScope.launch {
            setCommentsUseCase(getAllComments()).fold(
                { sideEffectChannel.send(Back) },
                { sideEffectChannel.send(Back) }
            )
        }
    }

    fun onSelectPage(page: PageType) {
        viewModelScope.launch {
            setCommentsUseCase(getAllComments()).fold(
                { sideEffectChannel.send(NavigateToPage(page)) },
                { sideEffectChannel.send(NavigateToPage(page)) }
            )
        }
    }

    private fun getAllComments() = state.value.comments
        .apply {
            if (state.value.editingComment.isNotEmpty())
                plus(state.value.editingComment)
        }
        .mapIndexed { index, instruction ->
            CreationComment(instruction, index.toShort())
        }
}
