package com.helpquest.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.regex.Pattern

enum class BuildVariants(val value: String) {
    MOCK("mock"),
    DEV("dev"),
    STAGE("stage"),
    PROD("prod"),
}

fun Project.configureBuildVariants() {
    extensions.configure<KotlinMultiplatformExtension> {
        val variantCapitalized = getCapitalizedBuildVariantString()
        sourceSets.apply {
            androidMain {
                kotlin.srcDir("src/androidMain$variantCapitalized/kotlin")
                resources.srcDir("src/androidMain$variantCapitalized/res")
            }
            commonMain {
                kotlin.srcDir("src/commonMain$variantCapitalized/kotlin")
                resources.srcDir("src/commonMain$variantCapitalized/composeResources")
            }
            iosMain {
                kotlin.srcDir("src/iosMain$variantCapitalized/kotlin")
            }
        }
    }
}

private fun Project.getAndroidBuildVariantOrNull(): BuildVariants? {
    val taskRequestsList = gradle.startParameter.taskRequests.flatMap { request ->
        request.args
    }
    if (taskRequestsList.isEmpty()) {
        return null
    }
    val taskRequestsStr = taskRequestsList.first()
    val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
        Pattern.compile("assemble(\\w+)")
    } else {
        Pattern.compile("bundle(\\w+)")
    }
    val matcher = pattern.matcher(taskRequestsStr)
    val variantName = if (matcher.find()) matcher.group(1).lowercase() else null

    return BuildVariants.values().find { it.value == variantName }
}

private fun getEnvBuildVariantOrNull(): BuildVariants? {
    val variants = BuildVariants.values().map { it.value }.toSet()
    val variantName = System.getenv()["VARIANT"]
        .toString()
        .takeIf { it in variants }
    return BuildVariants.values().find { it.value == variantName }
}

fun Project.currentBuildVariant(): BuildVariants =
    getAndroidBuildVariantOrNull()
        ?: getEnvBuildVariantOrNull()
        ?: BuildVariants.DEV

fun Project.getCapitalizedBuildVariantString(): String {
    //TODO: understand if should be created a different folder for each variant
    val buildVariant = when (currentBuildVariant()) {
        BuildVariants.MOCK -> BuildVariants.MOCK
        BuildVariants.DEV,
        BuildVariants.STAGE,
        BuildVariants.PROD -> BuildVariants.DEV
    }
    return buildVariant.value.replaceFirstChar { it.uppercaseChar() }
}

fun Project.getManifestVariantString(): String =
    currentBuildVariant().value.replaceFirstChar { it.uppercaseChar() }