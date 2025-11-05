package com.helpquest.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.getLib(name: String) = libs.findLibrary(name).get()
internal fun Project.getBundle(name: String) = libs.findBundle(name).get()
internal fun Project.getProjectApplicationId() =
    libs.findVersion("projectApplicationId").get().toString()

internal fun Project.getProjectCompileSdkVersion() =
    libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

internal fun Project.getProjectMinSdkVersion() =
    libs.findVersion("projectMinSdkVersion").get().toString().toInt()

internal fun Project.getProjectTargetSdkVersion() =
    libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

internal fun Project.getProjectVersionCode() =
    libs.findVersion("projectVersionCode").get().toString().toInt()

internal fun Project.getProjectVersionName() =
    libs.findVersion("projectVersionName").get().toString()

internal fun getProjectJavaVersion() = JavaVersion.VERSION_21
internal fun getProjectJvmTarget() = JvmTarget.JVM_21