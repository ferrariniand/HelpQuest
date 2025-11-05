import com.helpquest.convention.getBundle
import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpFeatureDataConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.helpquest.convention.kmp.library")
            }

            dependencies {
                "commonMainImplementation"(project(":core:domain"))
                "commonMainImplementation"(project(":core:data"))

                "commonMainImplementation"(getBundle("ktor-common"))
                "commonMainImplementation"(getLib("koin-core"))
            }
        }
    }
}