
plugins {
    alias(libs.plugins.convention.kmp.feature.data)
    alias(libs.plugins.convention.buildkonfig)
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
                // Add KMP dependencies here

                implementation(projects.feature.quest.domain)
                implementation(projects.core.database)
            }
        }

        val mobileMain by getting {
            dependsOn(commonMain.get())
        }
        androidMain {
            dependsOn(mobileMain)
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependsOn(mobileMain)
        }
    }

}