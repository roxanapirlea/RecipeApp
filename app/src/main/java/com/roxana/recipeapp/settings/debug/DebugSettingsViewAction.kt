package com.roxana.recipeapp.settings.debug

sealed class DebugSettingsViewAction

object Back : DebugSettingsViewAction()
data class SetOnAddRecipeOnboarding(val isDone: Boolean) : DebugSettingsViewAction()