@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.helpquest.convention


import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

internal fun Project.configureAndroidTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            compilerOptions {
                jvmTarget.set(getProjectJvmTarget())
            }

            unitTestVariant {
                sourceSetTree.set(KotlinSourceSetTree.unitTest)

                project.dependencies {
                    "implementation"(getLib("test-core-ktx"))
                    "implementation"(getLib("androidx-compose-ui-test-junit4"))
                    "debugImplementation"(
                        getLib("androidx-compose-ui-test-manifest")
                    )

                }
            }

            instrumentedTestVariant {
                sourceSetTree.set(KotlinSourceSetTree.test)

                project.dependencies {
                    "implementation"(getLib("test-core-ktx"))
                    "implementation"(getLib("androidx-compose-ui-test-junit4"))
                    "debugImplementation"(
                        getLib("androidx-compose-ui-test-manifest")
                    )

                }
            }
        }
    }
}