package com.roxana.recipeapp.misc

fun Double.toFormattedString(): String =
    if (this - this.toLong() == 0.0)
        String.format("%d", this.toLong())
    else
        String.format("%s", this)

fun Short?.toNotNull(default: Short): Short = this ?: default
