package com.roxana.recipeapp.edit.choosephoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.ClearPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.GetPhotoUseCase
import com.roxana.recipeapp.domain.editrecipe.IsRecipeExistingUseCase
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
) : ViewModel() {
    private val _state = MutableStateFlow(EditRecipeChoosePhotoViewState())
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

    fun onBack() {
        state.value.photoPath?.let {
            viewModelScope.launch {
                setPhotoUseCase(it).fold(
                    { _state.update { it.copy(navigation = Navigation.Back) } },
                    { _state.update { it.copy(navigation = Navigation.Back) } }
                )
            }
        } ?: _state.update { it.copy(navigation = Navigation.Back) }
    }

    fun onSelectPage(page: PageType) {
        state.value.photoPath?.let {
            viewModelScope.launch {
                setPhotoUseCase(it).fold(
                    {
                        _state.update {
                            it.copy(
                                navigation = Navigation.ToPage(
                                    page,
                                    state.value.isExistingRecipe
                                )
                            )
                        }
                    },
                    {
                        _state.update {
                            it.copy(
                                navigation = Navigation.ToPage(
                                    page,
                                    state.value.isExistingRecipe
                                )
                            )
                        }
                    }
                )
            }
        } ?: _state.update {
            it.copy(navigation = Navigation.ToPage(page, state.value.isExistingRecipe))
        }
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
