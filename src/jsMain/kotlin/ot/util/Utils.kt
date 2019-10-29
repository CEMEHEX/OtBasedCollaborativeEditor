package ot.util

fun <T> T?.validateNotNull(): T =
    this ?: throw IllegalStateException("required html elements not initialized")