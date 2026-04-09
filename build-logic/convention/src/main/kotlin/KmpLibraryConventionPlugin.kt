import com.helpquest.convention.applyHierarchyTemplate
import com.helpquest.convention.configureKotlinMultiplatform
import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlinx.kover")
            }

            configureKotlinMultiplatform()

            extensions.configure<KotlinMultiplatformExtension> {
                applyHierarchyTemplate()

                tasks.register("testClasses")
                kotlinExtension.sourceSets.findByName("androidDeviceTest")
                kotlinExtension.sourceSets.findByName("androidUnitTest")
                kotlinExtension.sourceSets.findByName("iosSimulatorArm64Test")

            }

            dependencies {
                "commonMainImplementation"(getLib("kotlin-stdlib"))
                "commonMainImplementation"(getLib("kotlinx-serialization-json"))
                "commonMainImplementation"(getLib("touchlab-kermit"))

                "commonTestImplementation"(getLib("kotlin-test"))
                "commonTestImplementation"(getLib("kotlin-test-annotations-common"))
                "commonTestImplementation"(getLib("kotlinx-coroutines-test"))
                "commonTestImplementation"(getLib("assertk"))
                "commonTestImplementation"(getLib("turbine"))
                "commonTestImplementation"(getLib("koin-test"))

                "androidMainImplementation"(getLib("androidx-runner"))
                "androidMainImplementation"(getLib("androidx-test-core"))
                "androidMainImplementation"(getLib("androidx-junit"))
            }
        }
    }
}