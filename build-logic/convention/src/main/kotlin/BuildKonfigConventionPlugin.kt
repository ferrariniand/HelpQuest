import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.helpquest.convention.currentBuildVariant
import com.helpquest.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra

class BuildKonfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            project.extra.set("buildkonfig.flavor", currentBuildVariant().id)

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()
                defaultConfigs {
                    //TODO: DEFINE API KEY and other BUILD CONFIGS
                    val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
                        .getProperty("API_KEY")
                        ?: throw IllegalStateException(
                            "Missing API_KEY property in local.properties"
                        )
                    buildConfigField(Type.STRING, "API_KEY", apiKey)
                    buildConfigField(Type.BOOLEAN, "useMockServer", "false")

                }
                defaultConfigs(Flavors.Environment.MOCK.id) {
                    buildConfigField(Type.STRING, "FLAVOR_ENV", Flavors.Environment.MOCK.id)
                    buildConfigField(Type.BOOLEAN, "useMockServer", "true")
                }
                defaultConfigs(Flavors.Environment.DEV.id) {
                    buildConfigField(Type.STRING, "FLAVOR_ENV", Flavors.Environment.DEV.id)
                }
                defaultConfigs(Flavors.Environment.STAGE.id) {
                    buildConfigField(Type.STRING, "FLAVOR_ENV", Flavors.Environment.STAGE.id)
                }
                defaultConfigs(Flavors.Environment.PROD.id) {
                    buildConfigField(Type.STRING, "FLAVOR_ENV", Flavors.Environment.PROD.id)
                }
            }
        }
    }
}