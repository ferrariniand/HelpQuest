import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.helpquest.convention.BuildVariants
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

            project.extra.set("buildkonfig.flavor", currentBuildVariant().value)

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()
                defaultConfigs {
                    //TODO: DEFINE API KEY and other BUILD CONFIGS
                    val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
                        .getProperty("API_KEY")
                        ?: throw IllegalStateException(
                            "Missing API_KEY property in local.properties"
                        )
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                }
                defaultConfigs(BuildVariants.MOCK.value) {
                    buildConfigField(FieldSpec.Type.STRING, "BUILD_TYPE", BuildVariants.MOCK.value)
                }
                defaultConfigs(BuildVariants.DEV.value) {
                    buildConfigField(FieldSpec.Type.STRING, "BUILD_TYPE", BuildVariants.DEV.value)
                }
                defaultConfigs(BuildVariants.STAGE.value) {
                    buildConfigField(FieldSpec.Type.STRING, "BUILD_TYPE", BuildVariants.STAGE.value)
                }
                defaultConfigs(BuildVariants.PROD.value) {
                    buildConfigField(FieldSpec.Type.STRING, "BUILD_TYPE", BuildVariants.PROD.value)
                }
            }
        }
    }
}