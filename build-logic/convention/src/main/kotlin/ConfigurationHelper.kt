import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.NamedDomainObjectContainer
import java.io.File
import java.util.Locale

object ConfigurationHelper {

    val FLAVORS_ENVIRONMENT = Flavors.ALL_ENVIRONMENTS.map { env ->
        env.id.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }
    val FLAVORS_BUILD_TYPE = Flavors.BuildType.values().map { buildType ->
        buildType.value.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }

    /**
     * Represents a configuration for flavor components in a build variant.
     *
     * This data class encapsulates the three main dimensions of a flavor configuration:
     * Environment flavor and BuildType.
     *
     * @property env The Environment flavor component of the flavor configuration. Defaults to an empty string.
     * @property buildType The BuildType component of the flavor configuration. Defaults to "release".
     */
    //TODO: MAYBE CAN BE REMOVED BECAUSE THE BUILD TYPE IS NOT USED
    data class FlavorConfig(
        val env: String = "",
        val buildType: String = "release",
    ) {
        fun isDebugEnv() = arrayOf(
            Flavors.Environment.MOCK.id,
            Flavors.Environment.DEV.id,
            Flavors.Environment.STAGE.id,
        ).contains(env.lowercase())

        fun isReleaseEnv() = arrayOf(
            Flavors.Environment.PROD.id
        ).contains(env.lowercase())

        fun filterValidConfig(): Boolean =
            (isDebugEnv() && "debug" == buildType)
                    || (isReleaseEnv() && "release" == buildType)
    }

    /**
     * Adds sub-flavor resources to the specified source sets based on flavor dimensions.
     *
     * This function processes the provided source sets and adds resource directories
     * for combinations of Environment flavor dimensions.
     *
     * @param sourceSets The container of Android source sets to be processed.
     * @param flavorDimensions A list of lists containing flavor dimensions. The first list
     *                         represents Environment and buildType dimensions.
     */
    fun addAndroidApplicationSubFlavorResources(
        sourceSets: NamedDomainObjectContainer<out AndroidSourceSet>,
        flavorDimensions: List<List<String>>,
        projectDir: File? = null
    ) {

        val dimensionsEnvironment = flavorDimensions[0]
        val dimensionsBuildType = flavorDimensions[1]


        sourceSets.matching { sourceSet ->
            val flavorConfig =
                extractFlavorConfig(sourceSet.name, dimensionsEnvironment, dimensionsBuildType)
            flavorConfig.env.isNotBlank() && flavorConfig.buildType.isNotBlank()
        }.all {
            val flavorConfig = extractFlavorConfig(name, dimensionsEnvironment, dimensionsBuildType)
            //TODO ADD CONFIG FOR EACH SOURCESET??? (androidMain, commonMain, ecc)
            res.srcDir("src/${flavorConfig.env.capitalize()}/res")

            if (projectDir != null) {
                val expectedName = flavorConfig.env.lowercase()
                if (name == expectedName) {
                    val combinationManifestDirs = listOf(
                        "src/${flavorConfig.env.capitalize()}",
                    )
                    for (dir in combinationManifestDirs) {
                        val manifestFile = File(projectDir, "$dir/AndroidManifest.xml")
                        if (manifestFile.exists()) {
                            manifest.srcFile(manifestFile)
                        }
                    }
                }
            }
        }
    }

    /**
     * Extracts flavor configuration from a given name and dimension lists.
     *
     * This function parses the provided name to identify Environment flavor and build type components
     * based on the supplied dimension lists.
     *
     * @param name The name to extract flavor configuration from. Expected to be in camelCase format.
     * @param dimensionsEnvironment List of possible Environment dimension values.
     * @param dimensionsBuildType List of possible BuildType dimension values.
     * @return A [FlavorConfig] object containing the extracted Environment flavor components.
     */
    private fun extractFlavorConfig(
        name: String,
        dimensionsEnvironment: List<String>,
        dimensionsBuildType: List<String>,
    ): FlavorConfig {
        val splitDimensions = name.split(Regex("(?=\\p{Lu})")).map { it.capitalize() }
        val environment = splitDimensions.find { it in dimensionsEnvironment }.orEmpty()
        val buildType = splitDimensions.find { it in dimensionsBuildType }.orEmpty()

        return FlavorConfig(environment, buildType)
    }

}

