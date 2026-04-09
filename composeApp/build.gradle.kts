import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.lint)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "com.helpquest.composeapp"
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 26

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

        androidResources {
            enable = true
        }
    }


    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "composeAppKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.compose)
            implementation(libs.jetbrains.compose.navigation)

            // Coil
            implementation(libs.coil.compose)

            //Koin
            implementation(libs.bundles.koin.common)

            // Core Modules
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.database)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            // Auth Modules
            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.data)
            implementation(projects.feature.auth.presentation)
            // Profile Modules
            implementation(projects.feature.profile.domain)
            implementation(projects.feature.profile.data)
            implementation(projects.feature.profile.presentation)
            // Quests Modules
            implementation(projects.feature.quest.domain)
            implementation(projects.feature.quest.data)
            implementation(projects.feature.quest.presentation)
            // Chat Modules
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.presentation)
            // Settings Modules
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.data)
            implementation(projects.feature.settings.presentation)
            // Notification Modules
            implementation(projects.feature.notification.domain)
            implementation(projects.feature.notification.data)
            // Home Modules
            implementation(projects.feature.home.domain)
            implementation(projects.feature.home.presentation)
            }
        }

        commonTest {
            dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.annotations.common)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.assertk)
            implementation(libs.mockk.common)
            implementation(libs.koin.test)

            implementation(libs.turbine)

            // Core Test Module
            implementation(projects.core.test)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.junit)
            }
        }

//        val mobileMain by getting
        androidMain {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(commonMain.get())
        }

//        val desktopMain by getting
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutines.swing)
//            implementation(libs.kotlin.stdlib)
//            implementation(libs.koin.compose)
//            implementation(libs.koin.compose.viewmodel)
//            implementation(libs.jsystemthemedetector)
//
//
//            implementation(projects.core.presentation)
//        }
//        val desktopTest by getting
//        desktopTest.dependencies {
//            implementation(compose.desktop.currentOs)
//        }
    }

}

compose.desktop {
    application {
        mainClass = "com.helpquest.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.helpquest"
            packageVersion = "1.0.0"
        }
    }
}
