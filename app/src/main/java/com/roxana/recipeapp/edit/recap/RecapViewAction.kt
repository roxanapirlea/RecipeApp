package com.roxana.recipeapp.edit.recap

sealed class RecapViewAction

object Back : RecapViewAction()
object Edit : RecapViewAction()
object Save : RecapViewAction()
object Finish : RecapViewAction()
