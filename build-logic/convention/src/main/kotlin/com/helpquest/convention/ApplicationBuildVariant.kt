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

fun Project.configureBuildVariants(shouldCreateAllVariants: Boolean = false) {
    extensions.configure<KotlinMultiplatformExtension> {
        val variantCapitalized = getBuildVariantCapitalizedString(shouldCreateAllVariants)
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

//            getByName("commonMain").apply {
//                val variantDir = "src/commonMain$variantCapitalized/kotlin"
//                kotlin.srcDir("src/commonMain$variantCapitalized/kotlin")
//                resources.srcDir("src/commonMain$variantCapitalized/composeResources")
//
//                println("Configuring Build Variant: $variantCapitalized -> Added $variantDir to commonMain")
//            }
//            //            androidMain {
//            findByName("androidMain")?.apply {
//                kotlin.srcDir("src/androidMain$variantCapitalized/kotlin")
//                resources.srcDir("src/androidMain$variantCapitalized/res")
//            }
//            findByName("iosMain")?.apply {
//                kotlin.srcDir("src/iosMain$variantCapitalized/kotlin")
//            }
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
    val pattern = Pattern.compile("(?:assemble|bundle|generate|install)(\\w+)")
    val matcher = pattern.matcher(taskRequestsStr)
    if (matcher.find()) {
        val fullVariantName = matcher.group(1).lowercase()
        val variant = BuildVariants.values().find { fullVariantName.contains(it.value) }
        println("getAndroidBuildVariantOrNull return ${variant?.name}")
        return variant
    }

    println("getAndroidBuildVariantOrNull return NULL")
    return null
}

private fun Project.getEnvBuildVariantOrNull(): BuildVariants? {
//    val variants = BuildVariants.values().map { it.value }.toSet()

//
//
//    // 2. If no tasks are running (we are just Syncing),
//    // Android Studio often passes the variant in a system property
//    val activeVariant = if (taskNames.isEmpty()) {
//        // This is a special property Android Studio sets during Sync
//        System.getProperty("android.package.name.variant")
//    } else {
//        taskNames.firstOrNull()
//    } ?: return null
//    println("[getEnvBuildVariantOrNull] activeVariant=$activeVariant")
//
    // 1. Check for Gradle "variant" Property (e.g., ./gradlew -Pvariant=dev)
    val propertyVariant = findProperty("variant")?.toString()?.lowercase()
    println("[getEnvBuildVariantOrNull] propertyVariant=$propertyVariant")
//
    // 2. Check for Gradle "env" Property (e.g., ./gradlew -Penv=dev)
    val envVariant = findProperty("env")?.toString()?.lowercase()
    println("[getEnvBuildVariantOrNull] envVariant=$envVariant")
//
    val variantName = propertyVariant ?: envVariant

    val variant = BuildVariants.values().find { it.value == variantName }
    println("getEnvBuildVariantOrNull return ${variant?.name}")
    return variant
}

fun Project.currentBuildVariant(): BuildVariants =
    getAndroidBuildVariantOrNull()
        ?: getEnvBuildVariantOrNull()
        ?: BuildVariants.DEV

fun Project.getBuildVariantCapitalizedString(shouldCreateAllVariants: Boolean): String {
    val currentBuildVariant = currentBuildVariant()
    //if shouldCreateAllVariants, use the current build variant;
    // else use "mock" for MOCK and "dev" for all the other build variants
    val buildVariant = when {
        shouldCreateAllVariants || (currentBuildVariant == BuildVariants.MOCK) -> {
            currentBuildVariant
        }

        else -> BuildVariants.DEV
    }
    return buildVariant.value.replaceFirstChar { it.uppercaseChar() }
}

fun Project.getManifestVariantString(): String =
    currentBuildVariant().value.replaceFirstChar { it.uppercaseChar() }