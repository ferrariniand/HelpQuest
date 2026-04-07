package com.helpquest.convention

import ConfigurationHelper
import Flavors
import Flavors.applyFlavors
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


internal fun Project.configureAndroidAppFlavors() {
    extensions.configure<ApplicationExtension> {
        val flavorConfig = Flavors.Configuration.Builder().withEnvironments(
            Flavors.Environment.MOCK,
            Flavors.Environment.DEV,
            Flavors.Environment.STAGE,
            Flavors.Environment.PROD,

            ).withEnvironmentApplicationIdSuffixes()
            .withDimensions(Flavors.ENVIRONMENT_DIMENSION).build()
        applyFlavors(flavorConfig)

        ConfigurationHelper.addSubFlavorResources(
            sourceSets,
            listOf(
                ConfigurationHelper.FLAVORS_ENVIRONMENT,
                ConfigurationHelper.FLAVORS_BUILD_TYPE
            )
        )
    }
    extensions.configure<ApplicationAndroidComponentsExtension> {
        beforeVariants { builder ->
            val isDebugEnv = arrayOf(
                Flavors.Environment.MOCK.id,
                Flavors.Environment.DEV.id,
                Flavors.Environment.STAGE.id,
            ).contains(builder.flavorName?.lowercase())


            if (isDebugEnv && builder.buildType == Flavors.BuildType.RELEASE.value) {
                builder.enable = false
            }
        }
    }

}