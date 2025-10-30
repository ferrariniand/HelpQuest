import com.helpquest.convention.configureAndroidTarget
import com.helpquest.convention.configureBuildVariants
import com.helpquest.convention.configureDesktopTarget
import com.helpquest.convention.configureIosTargets
import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.helpquest.convention.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlinx.kover")
            }

            configureAndroidTarget()
            configureIosTargets()
            configureDesktopTarget()

            dependencies {
                "debugImplementation"(getLib("androidx-compose-ui-tooling"))
            }

            configureBuildVariants()

        }
    }
}