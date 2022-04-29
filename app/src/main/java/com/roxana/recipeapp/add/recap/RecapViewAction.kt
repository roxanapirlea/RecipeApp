package com.roxana.recipeapp.add.recap

sealed class RecapViewAction

object Back : RecapViewAction()
object Edit : RecapViewAction()
object Save : RecapViewAction()
object Finish : RecapViewAction()
