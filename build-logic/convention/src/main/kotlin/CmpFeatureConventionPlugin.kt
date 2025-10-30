import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpFeatureConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.helpquest.convention.cmp.library")
            }

            dependencies {
                "commonMainImplementation"(project(":core:presentation"))
                "commonMainImplementation"(project(":core:designsystem"))

                "commonMainImplementation"(platform(getLib("koin-bom")))
                "androidMainImplementation"(platform(getLib("koin-bom")))

                "commonMainImplementation"(getLib("koin-compose"))
                "commonMainImplementation"(getLib("koin-compose-viewmodel"))

                "commonMainImplementation"(getLib("jetbrains-compose-runtime"))
                "commonMainImplementation"(getLib("jetbrains-compose-viewmodel"))
                "commonMainImplementation"(getLib("jetbrains-lifecycle-viewmodel"))
                "commonMainImplementation"(getLib("jetbrains-lifecycle-compose"))

                "commonMainImplementation"(getLib("jetbrains-lifecycle-viewmodel-savedstate"))
                "commonMainImplementation"(getLib("jetbrains-savedstate"))
                "commonMainImplementation"(getLib("jetbrains-bundle"))
                "commonMainImplementation"(getLib("jetbrains-compose-navigation"))
                "commonMainImplementation"(getLib("jetbrains-compose-backhandler"))

                "androidMainImplementation"(getLib("koin-android"))
                "androidMainImplementation"(getLib("koin-androidx-compose"))
                "androidMainImplementation"(getLib("koin-androidx-navigation"))
                "androidMainImplementation"(getLib("koin-core-viewmodel"))
            }
        }
    }
}