@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.helpquest.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate

private val hierarchyTemplate = KotlinHierarchyTemplate {
    common {
        group("mobile") {
            withAndroidTarget()
            group("ios") {
                withIos()
            }
        }

        group("jvmCommon") {
            withAndroidTarget()
            withJvm()
        }

        group("native") {
            withNative()

            group("apple") {
                withApple()

                group("ios") {
                    withIos()
                }

                group("macos") {
                    withMacos()
                }
            }
        }
    }
}

fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}