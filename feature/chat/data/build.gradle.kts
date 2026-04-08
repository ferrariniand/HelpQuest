plugins {
    alias(libs.plugins.convention.kmp.feature.data)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    kover {
        reports {
            filters {
                excludes {
                    packages(
                        "com.helpquest.chat.data.di",
                        "com.helpquest.chat.data.dto",
                    )
                }
            }
        }
    }
    androidLibrary {
        namespace = "com.helpquest.feature.chat.data"
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
                implementation(libs.kotlinx.datetime)

                implementation(projects.feature.chat.domain)
                implementation(projects.core.database)
            }
        }

        androidMain {
            dependsOn(commonMain.get())
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(libs.koin.android)
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
    }
}