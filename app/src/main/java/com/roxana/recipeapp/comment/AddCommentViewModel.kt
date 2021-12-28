package com.roxana.recipeapp.comment

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddCommentViewModel @Inject constructor() : ViewModel() {

    @VisibleForTesting
    val _state = MutableStateFlow<String>("Hello")
    val state: StateFlow<String> = _state.asStateFlow()
}