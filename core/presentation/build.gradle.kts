
plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {

    // Apply the default hierarchy again. It'll create, for example, the iosMain source set:
    applyDefaultHierarchyTemplate()

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here
                implementation(compose.components.resources)

                implementation(libs.material3.adaptive)
                implementation(libs.jetbrains.lifecycle.compose)
                implementation(libs.bundles.koin.common)
                implementation(libs.kotlinx.datetime)


                implementation(projects.core.domain)
            }
        }

        val mobileMain by getting {
            dependencies {
                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.notifications)

            }
        }

        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(projects.core.domain)
            }
        }

        iosMain {
            dependsOn(mobileMain)
        }
    }

}

compose.resources {
    publicResClass = true
}