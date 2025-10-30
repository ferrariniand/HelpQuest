import com.android.build.api.dsl.LibraryExtension
import com.helpquest.convention.configureKotlinAndroid
import com.helpquest.convention.configureKotlinMultiplatform
import com.helpquest.convention.getLib
import com.helpquest.convention.pathToResourcePrefix
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlinx.kover")
            }

            configureKotlinMultiplatform()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                resourcePrefix = this@with.pathToResourcePrefix()

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        merges += "/META-INF/LICENSE.md"
                        merges += "/META-INF/LICENSE-notice.md"
                    }
                }
                testOptions {
                    packaging {
                        resources {
                            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                            merges += "/META-INF/LICENSE.md"
                            merges += "/META-INF/LICENSE-notice.md"
                        }
                    }
                }


                // Required to make debug build of app run in iOS simulator
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            extensions.configure<KotlinMultiplatformExtension> {
                tasks.register("testClasses")
                kotlinExtension.sourceSets.findByName("androidDeviceTest")
                kotlinExtension.sourceSets.findByName("androidUnitTest")
                kotlinExtension.sourceSets.findByName("iosSimulatorArm64Test")

                sourceSets.getByName("commonMain") {
                    dependencies {
                        implementation(getLib("kotlin-stdlib"))
                        implementation(getLib("kotlinx-serialization-json"))
                        implementation(getLib("touchlab-kermit"))

                    }
                }
                sourceSets.getByName("commonTest") {
                    dependencies {
                        implementation(getLib("kotlin-test"))
                        implementation(getLib("kotlin-test-annotations-common"))
                        implementation(getLib("kotlinx-coroutines-test"))
                        implementation(getLib("assertk"))
                        implementation(getLib("turbine"))
                        implementation(getLib("koin-test"))
                    }
                }
                sourceSets.getByName("androidMain") {
                    dependencies {
                        implementation(getLib("androidx-runner"))
                        implementation(getLib("androidx-test-core"))
                        implementation(getLib("androidx-junit"))

                        implementation(getLib("mockk"))
                        implementation(getLib("mockk-android"))
                    }
                }
            }

        }
    }
}