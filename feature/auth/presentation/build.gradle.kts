plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    kover {
        reports {
            filters {
                excludes {
                    annotatedBy("androidx.compose.runtime.Composable")
                    classes(
                        "com.helpquest.auth.presentation.*.*Action",
                        "com.helpquest.auth.presentation.*.*Event",
                        "com.helpquest.auth.presentation.*.*State",
                    )
                    packages(
                        "com.helpquest.auth.presentation.di",
                        "com.helpquest.auth.presentation.navigation",
                    )
                }
            }
        }
    }
    androidLibrary {
        namespace = "com.helpquest.feature.auth.presentation"
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

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                // Add KMP dependencies here
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(projects.feature.auth.domain)
            }
        }

        commonTest {
            dependencies {
                // Core Test Module
                implementation(projects.core.test)
            }
        }

        androidMain {
            dependsOn(commonMain.get())
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }

}