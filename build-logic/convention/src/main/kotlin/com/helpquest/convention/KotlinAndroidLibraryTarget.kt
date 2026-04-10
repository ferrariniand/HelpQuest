@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.helpquest.convention


import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureAndroidLibraryTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
            compileSdk = 36
            minSdk = 26
            namespace = pathToPackageName()

            withHostTestBuilder {
            }

            withDeviceTestBuilder {
                sourceSetTreeName = "test"
            }.configure {
                instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
            androidResources.enable = true
        }
    }

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
    }
}