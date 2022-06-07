package com.roxana.recipeapp.domain.common

interface Logger {
    fun d(message: String?)
    fun d(tag: String, message: String?)
    fun d(t: Throwable?, message: String? = null)
    fun d(tag: String, t: Throwable?, message: String? = null)
    fun v(message: String?)
    fun v(tag: String, message: String?)
    fun v(t: Throwable?, message: String? = null)
    fun v(tag: String, t: Throwable?, message: String? = null)
    fun i(message: String?)
    fun i(tag: String, message: String?)
    fun i(t: Throwable?, message: String? = null)
    fun i(tag: String, t: Throwable?, message: String? = null)
    fun w(message: String?)
    fun w(tag: String, message: String?)
    fun w(t: Throwable?, message: String? = null)
    fun w(tag: String, t: Throwable?, message: String? = null)
    fun e(message: String?)
    fun e(tag: String, message: String?)
    fun e(t: Throwable?, message: String? = null)
    fun e(tag: String, t: Throwable?, message: String? = null)
}