package com.example.drag.utils

import android.os.Handler
import android.os.Looper

fun runDelayed(millis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, millis)
}

fun <T> confinedLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)