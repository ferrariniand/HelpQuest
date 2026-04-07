package com.helpquest.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

//TODO: NOT USED!! TO BE REMOVED??
internal fun Project.configureAndroidManifest(
    applicationExtension: ApplicationExtension
) {
    with(applicationExtension) {
        val variant = getManifestVariantString()
        sourceSets.getByName("main") {
            manifest.srcFile("src/$variant/AndroidManifest.xml")
            res.srcDirs("src/$variant/res")
        }
    }
}