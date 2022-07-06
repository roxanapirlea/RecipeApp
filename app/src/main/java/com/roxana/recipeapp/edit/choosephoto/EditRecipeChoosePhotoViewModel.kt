package com.roxana.recipeapp.edit.choosephoto

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.ClearPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
import com.roxana.recipeapp.domain.editrecipe.ResetRecipeUseCase
import com.roxana.recipeapp.domain.editrecipe.SetPhotoUseCase
import com.roxana.recipeapp.edit.PageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeChoosePhotoViewModel @Inject constructor(
    private val isRecipeExistingUseCase: IsRecipeExistingUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
    private val setPhotoUseCase: SetPhotoUseCase,
    private val clearPhotoUseCase: ClearPhotoUseCase,
    private val resetRecipeUseCase: ResetRecipeUseCase,
) : ViewModel() {
    @VisibleForTesting
    val _state = MutableStateFlow(EditRecipeChoosePhotoViewState())
    val state: StateFlow<EditRecipeChoosePhotoViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getPhotoUseCase(null)
                .map { photoResult ->
                    val isExistingRecipe = isRecipeExistingUseCase(null).getOrDefault(false)
                    photoResult.getOrNull() to isExistingRecipe
                }.collect { (photoPath, isExistingRecipe) ->
                    _state.value = EditRecipeChoosePhotoViewState(photoPath, isExistingRecipe)
                }
        }
    }

    fun onValidate() {
        state.value.photoPath?.let {
            viewModelScope.launch {
                setPhotoUseCase(it).fold(
                    { sendForwardEvent() },
                    { sendForwardEvent() }
                )
            }
        } ?: sendForwardEvent()
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
        state.value.photoPath?.let {
            viewModelScope.launch {
                setPhotoUseCase(it).fold(
                    { _state.update { it.copy(navigation = Navigation.Close) } },
                    { _state.update { it.copy(navigation = Navigation.Close) } }
                )
            }
        } ?: _state.update { it.copy(navigation = Navigation.Close) }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    fun onCheckShouldClose() {
        _state.update { it.copy(showSaveDialog = true) }
    }

    fun onSelectPage(page: PageType) {
        state.value.photoPath?.let {
            viewModelScope.launch {
                setPhotoUseCase(it).fold(
                    { _state.update { it.copy(navigation = Navigation.ToPage(page)) } },
                    { _state.update { it.copy(navigation = Navigation.ToPage(page)) } }
                )
            }
        } ?: _state.update { it.copy(navigation = Navigation.ToPage(page)) }
    }

    fun onRecapturePhoto() {
        state.value.photoPath?.let {
            viewModelScope.launch {
                clearPhotoUseCase(it).fold(
                    { _state.update { it.copy(navigation = Navigation.PhotoCapture) } },
                    { _state.update { it.copy(navigation = Navigation.PhotoCapture) } }
                )
            }
        } ?: _state.update { it.copy(navigation = Navigation.PhotoCapture) }
    }

    fun onClearPhoto() {
        state.value.photoPath?.let {
            viewModelScope.launch {
                clearPhotoUseCase(it)
            }
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigation = null) }
    }
}
