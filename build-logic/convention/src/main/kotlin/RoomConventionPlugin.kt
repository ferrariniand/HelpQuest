import androidx.room.gradle.RoomExtension
import com.helpquest.convention.getLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "commonMainApi"(getLib("androidx-room-runtime"))
                "commonMainApi"(getLib("sqlite-bundled"))
                "androidMainApi"(getLib("androidx-room-runtime"))
                "kspAndroid"(getLib("androidx-room-compiler"))
                "kspIosSimulatorArm64"(getLib("androidx-room-compiler"))
                "kspIosArm64"(getLib("androidx-room-compiler"))
                "kspIosX64"(getLib("androidx-room-compiler"))
                "kspDesktop"(getLib("androidx-room-compiler"))
            }
        }
    }
}