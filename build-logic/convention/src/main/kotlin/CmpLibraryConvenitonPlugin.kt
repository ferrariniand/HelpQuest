import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.helpquest.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(getLib("jetbrains-compose-ui"))
                "commonMainImplementation"(getLib("jetbrains-compose-foundation"))
                "commonMainImplementation"(getLib("jetbrains-compose-material3"))
                "commonMainImplementation"(getLib("jetbrains-compose-material-icons-core"))
                "commonMainImplementation"(getLib("jetbrains-compose-components-resources"))

                "debugImplementation"(getLib("androidx-compose-ui-tooling"))
            }
        }
    }
}