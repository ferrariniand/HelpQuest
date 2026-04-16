import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.lint)
}

kotlin {

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
                implementation(compose.preview)
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
                implementation(libs.koin.test)
                implementation(libs.turbine)

                // Core Test Module
                implementation(projects.core.test)
            }
        }

        val mobileMain by getting
        androidMain {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(commonMain.get())
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jsystemthemedetector)


            implementation(projects.core.presentation)
        }
        val desktopTest by getting
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
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
