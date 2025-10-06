@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.helpquest.convention


import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

internal fun Project.configureAndroidTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }

            instrumentedTestVariant {
                sourceSetTree.set(KotlinSourceSetTree.test)

                dependencies {
                    "implementation"(libs.findLibrary("test-core-ktx").get())
                    "implementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
                    "debugImplementation"(
                        libs.findLibrary("androidx-compose-ui-test-manifest").get()
                    )

                }
            }
        }
    }
}