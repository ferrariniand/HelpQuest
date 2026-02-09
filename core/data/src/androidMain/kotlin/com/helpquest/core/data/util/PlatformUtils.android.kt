package com.helpquest.core.data.util

const val ANDROID_OS_NAME = "ANDROID"

actual object PlatformUtils {
    actual fun getOSName() = ANDROID_OS_NAME
}