package com.helpquest.convention

import org.gradle.api.Project

fun Project.pathToPackageName(): String {
    val relativePackageName = path
        .replace(':', '.')
        .lowercase()
    return "com.helpquest$relativePackageName"
}

fun Project.pathToResourcePrefix(): String {
    return path
        .replace(':', '_')
        .lowercase()
        .drop(1) + "_"
}

fun Project.pathToFrameworkName(): String {
    val parts = this.path.split(":", "-", "_", " ")
    val fn = parts.joinToString("") { part ->
        part.capitalize()
    }
    return fn
}

fun String.capitalize() = this.replaceFirstChar { it.uppercaseChar() }