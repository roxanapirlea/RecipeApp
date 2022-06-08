package com.roxana.recipeapp.common

import com.roxana.recipeapp.domain.common.Logger
import timber.log.Timber
import javax.inject.Inject

class TimberLogger @Inject constructor() : Logger {
    override fun d(message: String?) {
        Timber.d(message)
    }

    override fun d(tag: String, message: String?) {
        Timber.tag(tag).d(message)
    }

    override fun d(t: Throwable?, message: String?) {
        Timber.d(t, message)
    }

    override fun d(tag: String, t: Throwable?, message: String?) {
        Timber.tag(tag).d(t, message)
    }

    override fun v(message: String?) {
        Timber.v(message)
    }

    override fun v(tag: String, message: String?) {
        Timber.tag(tag).v(message)
    }

    override fun v(t: Throwable?, message: String?) {
        Timber.v(t, message)
    }

    override fun v(tag: String, t: Throwable?, message: String?) {
        Timber.tag(tag).v(t, message)
    }

    override fun i(message: String?) {
        Timber.i(message)
    }

    override fun i(tag: String, message: String?) {
        Timber.tag(tag).i(message)
    }

    override fun i(t: Throwable?, message: String?) {
        Timber.i(t, message)
    }

    override fun i(tag: String, t: Throwable?, message: String?) {
        Timber.tag(tag).i(t, message)
    }

    override fun w(message: String?) {
        Timber.w(message)
    }

    override fun w(tag: String, message: String?) {
        Timber.tag(tag).w(message)
    }

    override fun w(t: Throwable?, message: String?) {
        Timber.w(t, message)
    }

    override fun w(tag: String, t: Throwable?, message: String?) {
        Timber.tag(tag).w(t, message)
    }

    override fun e(message: String?) {
        Timber.e(message)
    }

    override fun e(tag: String, message: String?) {
        Timber.tag(tag).e(message)
    }

    override fun e(t: Throwable?, message: String?) {
        Timber.e(t, message)
    }

    override fun e(tag: String, t: Throwable?, message: String?) {
        Timber.tag(tag).e(t, message)
    }
}
