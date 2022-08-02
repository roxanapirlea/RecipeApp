package com.roxana.recipeapp.edit.photocapture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roxana.recipeapp.domain.editrecipe.SetPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoCaptureViewModel @Inject constructor(
    private val setPhotoUseCase: SetPhotoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PhotoCaptureViewState())
    val state: StateFlow<PhotoCaptureViewState> = _state.asStateFlow()

    fun onPhotoTaken(photoPath: String) {
        _state.update { it.copy(photoPath = photoPath) }
    }

    fun onRetakePhoto() {
        _state.update { it.copy(photoPath = null) }
    }

    fun onPhotoAdded() {
        val photoPath = state.value.photoPath ?: return
        viewModelScope.launch {
            setPhotoUseCase(photoPath).getOrNull()
            _state.update { it.copy(shouldNavigateBack = true) }
        }
    }

    fun onNavDone() {
        _state.update { it.copy(shouldNavigateBack = false) }
    }

    fun onConfigError() {
        _state.update { it.copy(isConfigError = true) }
    }

    fun onConfigErrorDismissed() {
        _state.update { it.copy(isConfigError = false) }
    }
}
