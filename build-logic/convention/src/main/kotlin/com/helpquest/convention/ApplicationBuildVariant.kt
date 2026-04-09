package com.helpquest.convention

import Flavors
import org.gradle.api.Project
import java.util.regex.Pattern

private fun Project.getAndroidBuildVariantOrNull(): Flavors.Environment? {
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
        val variant = Flavors.Environment.values().find { fullVariantName.contains(it.id) }
        println("getAndroidBuildVariantOrNull return ${variant?.name}")
        return variant
    }

    println("getAndroidBuildVariantOrNull return NULL")
    return null
}

private fun Project.getEnvBuildVariantOrNull(): Flavors.Environment? {
    // 1. Check for Gradle "variant" Property (e.g., ./gradlew -Pvariant=dev)
    val propertyVariant = findProperty("variant")?.toString()?.lowercase()
    println("[getEnvBuildVariantOrNull] propertyVariant=$propertyVariant")
//
    // 2. Check for Gradle "env" Property (e.g., ./gradlew -Penv=dev)
    val envVariant = findProperty("env")?.toString()?.lowercase()
    println("[getEnvBuildVariantOrNull] envVariant=$envVariant")
//
    val variantName = propertyVariant ?: envVariant

    val variant = Flavors.Environment.values().find { it.id == variantName }
    println("getEnvBuildVariantOrNull return ${variant?.name}")
    return variant
}

fun Project.currentBuildVariant(): Flavors.Environment =
    getAndroidBuildVariantOrNull()
        ?: getEnvBuildVariantOrNull()
        ?: Flavors.Environment.PROD

//TODO: ADD BACK WHEN IT WILL BE POSSIBLE TO IMPLEMENT BUILD VARIANTS AT MODULE LEVEL (CommonMainMock, CommonMainDev ...)
//fun Project.configureBuildVariants(shouldCreateAllVariants: Boolean = false) {
//    extensions.configure<KotlinMultiplatformExtension> {
//        val variant = getBuildVariant(shouldCreateAllVariants)
//        val variantCapitalized = variant.capitalize()
//        sourceSets.apply {
//            androidMain {
//                kotlin.srcDir("src/androidMain$variantCapitalized/kotlin")
//                resources.srcDir("src/androidMain$variantCapitalized/res")
//            }
//            commonMain {
//                kotlin.srcDir("src/commonMain$variantCapitalized/kotlin")
//                resources.srcDir("src/commonMain$variantCapitalized/composeResources")
//            }
//            iosMain {
//                kotlin.srcDir("src/iosMain$variantCapitalized/kotlin")
//            }
//        }
//    }
//}

//fun Project.getBuildVariant(shouldCreateAllVariants: Boolean): String {
//    val currentBuildVariant = currentBuildVariant()
//    //if shouldCreateAllVariants, use the current build variant;
//    // else use "mock" for MOCK and "dev" for all the other build variants
//    val buildVariant = when {
//        shouldCreateAllVariants || (currentBuildVariant == Flavors.Environment.MOCK) -> {
//            currentBuildVariant
//        }
//
//        else -> Flavors.Environment.DEV
//    }
//    return buildVariant.id
//}

//TODO: NOT USED!! TO BE REMOVED??
//internal fun Project.configureAndroidManifest(
//    applicationExtension: ApplicationExtension
//) {
//    with(applicationExtension) {
//        val variant = getManifestVariantString()
//        sourceSets.getByName("main") {
//            manifest.srcFile("src/$variant/AndroidManifest.xml")
//            res.srcDirs("src/$variant/res")
//        }
//    }
//}

//fun Project.getManifestVariantString(): String =
//    currentBuildVariant().id.capitalize()