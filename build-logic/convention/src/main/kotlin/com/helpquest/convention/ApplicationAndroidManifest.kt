package com.helpquest.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidManifest(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        val variantCapitalized = getManifestVariantString()
        sourceSets.getByName("main") {
            manifest.srcFile("src/androidMain$variantCapitalized/AndroidManifest.xml")
            res.srcDirs("src/androidMain$variantCapitalized/res")
        }
    }
}