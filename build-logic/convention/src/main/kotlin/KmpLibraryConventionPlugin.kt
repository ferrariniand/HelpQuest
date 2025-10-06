import com.android.build.api.dsl.LibraryExtension
import com.helpquest.convention.configureKotlinAndroid
import com.helpquest.convention.configureKotlinMultiplatform
import com.helpquest.convention.libs
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
                        implementation(libs.findLibrary("kotlin-stdlib").get())
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())
                        implementation(libs.findLibrary("touchlab-kermit").get())

                    }
                }
                sourceSets.getByName("commonTest") {
                    dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                        implementation(libs.findLibrary("kotlin-test-annotations-common").get())
                        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                        implementation(libs.findLibrary("assertk").get())
                        implementation(libs.findLibrary("turbine").get())
                        implementation(libs.findLibrary("mockk-common").get())
                    }
                }
                sourceSets.getByName("androidMain") {
                    dependencies {
                        implementation(libs.findLibrary("androidx-runner").get())
                        implementation(libs.findLibrary("androidx-test-core").get())
                        implementation(libs.findLibrary("androidx-junit").get())

                        implementation(libs.findLibrary("mockk").get())
                        implementation(libs.findLibrary("mockk-android").get())
                    }
                }
            }

        }
    }
}