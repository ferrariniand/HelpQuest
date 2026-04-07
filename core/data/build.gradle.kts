import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    kover {
        reports {
            filters {
                excludes {
                    classes(
                        "com.helpquest.core.data.networking.UrlConstants",
                        "com.helpquest.core.data.networking.HttpClientFactory",
                    )
                    packages(
                        "com.helpquest.core.data.logging",
                        "com.helpquest.core.data.di",
                        "com.helpquest.core.data.dto",
                    )
                }
            }
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
                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)
                implementation(libs.ksafe)
                implementation(libs.ksafe.compose)
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)


                implementation(projects.core.domain)
                implementation(projects.core.database)

            }
        }

        val jvmCommonMain by getting {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(jvmCommonMain)
            dependsOn(commonMain.get())
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(projects.core.domain)


                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
                implementation(libs.androidx.lifecycle.process)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
                implementation(libs.ktor.client.darwin)

            }
        }

        desktopMain {
            dependsOn(jvmCommonMain)
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }

    targets.withType<KotlinNativeTarget> {
        compilations.getByName("main") {
            cinterops {
                create("network") {
                    defFile(file("src/nativeInterop/cinterop/network.def"))
                }
            }
        }
    }

}