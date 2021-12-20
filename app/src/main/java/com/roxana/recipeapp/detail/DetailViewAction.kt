package com.roxana.recipeapp.detail

sealed class DetailViewAction

object Back : DetailViewAction()
object Edit : DetailViewAction()
object StartCooking : DetailViewAction()
object AddComment : DetailViewAction()
