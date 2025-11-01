package com.helpquest.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

enum class BuildType(val value: String) {
    DEBUG("debug"),
    RELEASE("release"),
}


internal fun Project.configureBuildTypes() {
    extensions.configure<ApplicationExtension> {
        buildTypes {
            create(BuildVariants.PROD.value) {
                initWith(getByName(BuildType.DEBUG.value))
                matchingFallbacks += listOf(BuildType.DEBUG.value)
                isMinifyEnabled = false
                //TODO define RELEASE CONFIGURATION AND SIGNING !!!!!
//                initWith(getByName(BuildType.RELEASE.value))
//                matchingFallbacks += listOf(BuildType.RELEASE.value)
//                isMinifyEnabled = true
                //TODO proguard
            }
            create(BuildVariants.STAGE.value) {
                initWith(getByName(BuildType.DEBUG.value))
                matchingFallbacks += listOf(BuildType.DEBUG.value)
                isMinifyEnabled = false
                //TODO define RELEASE CONFIGURATION AND SIGNING !!!!!
//                initWith(getByName(BuildType.RELEASE.value))
//                matchingFallbacks += listOf(BuildType.RELEASE.value)
                //TODO proguard
            }
            create(BuildVariants.DEV.value) {
                initWith(getByName(BuildType.DEBUG.value))
                matchingFallbacks += listOf(BuildType.DEBUG.value)
                isMinifyEnabled = false
            }
            create(BuildVariants.MOCK.value) {
                initWith(getByName(BuildType.DEBUG.value))
                matchingFallbacks += listOf(BuildType.DEBUG.value)
                isMinifyEnabled = false
            }
        }
    }
    extensions.configure<ApplicationAndroidComponentsExtension> {
        beforeVariants { builder ->
            if (builder.buildType == BuildType.DEBUG.value || builder.buildType == BuildType.RELEASE.value) {
                builder.enable = false
            }
        }
    }

}