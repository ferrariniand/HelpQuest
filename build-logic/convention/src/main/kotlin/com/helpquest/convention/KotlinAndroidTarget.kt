@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.helpquest.convention


import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

internal fun Project.configureAndroidLibraryTarget() {

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
    }
}