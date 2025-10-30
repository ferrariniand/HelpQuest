package com.helpquest.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureDesktopTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm("desktop") {
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        // Set up the Kotlin compiler options for the 'main' compilation:
                        jvmTarget.set(getProjectJvmTarget())
                    }
                }
            }
        }
    }
}