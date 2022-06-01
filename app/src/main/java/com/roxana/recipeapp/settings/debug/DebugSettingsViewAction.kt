package com.roxana.recipeapp.settings.debug

sealed class DebugSettingsViewAction

object Back : DebugSettingsViewAction()
data class SetOnEditRecipeOnboarding(val isDone: Boolean) : DebugSettingsViewAction()
