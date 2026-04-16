plugins {
    alias(libs.plugins.convention.cmp.feature)
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
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.preview)

                implementation(libs.material3.adaptive)
                implementation(libs.material3.adaptive.layout)
                implementation(libs.material3.adaptive.navigation)
                implementation(libs.kotlinx.datetime)

                implementation(projects.feature.chat.domain)
            }
        }

        commonTest {
            dependencies {
                // Core Test Module
                implementation(projects.core.test)
            }
        }

        val mobileMain by getting
        androidMain {
            dependsOn(mobileMain)
        }

        iosMain {
            dependsOn(mobileMain)
        }
    }

}