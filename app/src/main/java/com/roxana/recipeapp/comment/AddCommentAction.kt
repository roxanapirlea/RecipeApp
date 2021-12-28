package com.roxana.recipeapp.comment

sealed class AddCommentAction

data class CommentChanged(val comment: String) : AddCommentAction()
object SaveComment : AddCommentAction()
