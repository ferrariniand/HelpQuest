package com.helpquest.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    applicationExtension: ApplicationExtension
) {
    with(applicationExtension) {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = getLib("androidx-compose-bom")
            "implementation"(platform(bom))
            "testImplementation"(platform(bom))
            "debugImplementation"(getLib("androidx-compose-ui-tooling-preview"))
            "debugImplementation"(getLib("androidx-compose-ui-tooling"))
        }
    }
}