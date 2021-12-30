package com.roxana.recipeapp.misc

fun Double.toFormattedString(): String =
    if (this - this.toLong() == 0.0)
        String.format("%d", this.toLong())
    else if ((this * 10) - (this * 10).toLong() == 0.0)
        String.format("%.1f", this)
    else
        String.format("%.2f", this)

fun Short?.toNotNull(default: Short): Short = this ?: default
