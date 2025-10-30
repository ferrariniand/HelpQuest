package com.helpquest.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        compileSdk = getProjectCompileSdkVersion()

        defaultConfig.minSdk = getProjectMinSdkVersion()

        compileOptions {
            sourceCompatibility = getProjectJavaVersion()
            targetCompatibility = getProjectJavaVersion()
            isCoreLibraryDesugaringEnabled = true
        }

        configureKotlin()

        dependencies {
            "coreLibraryDesugaring"(getLib("android-desugarJdkLibs"))
        }
    }
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(getProjectJvmTarget())

            freeCompilerArgs.add(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}