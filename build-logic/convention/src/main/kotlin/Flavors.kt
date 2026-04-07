import com.android.build.api.dsl.ApplicationExtension

object Flavors {
    const val ENVIRONMENT_DIMENSION = "env"
    val ALL_ENVIRONMENTS = Environment.values().toList()
    val DEFAULT_ENVIRONMENTS = listOf(Environment.DEV, Environment.STAGE, Environment.PROD)

    val MATCHING_FALLBACKS_MAP = mapOf(
        Environment.MOCK to BuildType.DEBUG.value,
        Environment.DEV to BuildType.DEBUG.value,
        Environment.STAGE to BuildType.DEBUG.value,
        Environment.PROD to BuildType.DEBUG.value, //TODO set to BuildType.RELEASE.value when release is ready
    )

    class Configuration private constructor(
        val envApplicationIds: Map<Environment, String>,
        val envApplicationIdSuffixes: Map<Environment, String>,
        val matchingFallbacks: Map<Environment, String>,
        val environments: List<Environment>,
        val dimensions: List<String>
    ) {
        class Builder {
            private val envApplicationIds = mutableMapOf<Environment, String>()
            private val envApplicationIdSuffixes = mutableMapOf<Environment, String>()
            private val matchingFallbacks = MATCHING_FALLBACKS_MAP
            private var environments: List<Environment> = DEFAULT_ENVIRONMENTS
            private var dimensions: List<String> = listOf(ENVIRONMENT_DIMENSION)

            /**
             * set the applicationId-suffix for all Environments to the Environment-id
             */
            fun withEnvironmentApplicationIdSuffixes() = apply {
                Environment.values().forEach { env ->
                    if (env != Environment.PROD) {
                        envApplicationIdSuffixes[env] = env.id
                    }
                }
            }

            /**
             * override the list of Environments, e.g. if the app does not support all Environments
             */
            fun withEnvironments(vararg environments: Environment) =
                apply { this.environments = environments.toList() }


            /**
             * override the list of Dimensions, e.g. if the app does not support all dimensions or wants to add one
             * Note: If one of the default dimensions is missing, the customIgnoreChecker will be called with
             * default values for the missing dimension. If no customIgnoreChecker is provided, alle variants are used.
             */
            fun withDimensions(vararg dimensions: String) =
                apply { this.dimensions = dimensions.toList() }

            /**
             * create the Configuration
             */
            fun build() = Configuration(
                envApplicationIds,
                envApplicationIdSuffixes,
                matchingFallbacks,
                environments,
                dimensions
            )
        }
    }

    enum class Environment(
        val id: String,
    ) {
        MOCK("mock"),
        DEV("dev"),
        STAGE("stage"),
        PROD("prod")
        ;

        companion object {
            fun fromId(id: String) = Flavors.Environment.values().first { it.id == id }

            fun fromIdOrNull(id: String) = Flavors.Environment.values().firstOrNull { it.id == id }
        }
    }

    enum class BuildType(val value: String) {
        DEBUG("debug"),
        RELEASE("release"),
    }

    /**
     * activate and set the Flavors based on the given Configuration
     */

    fun <T : ApplicationExtension> T.applyFlavors(
        configuration: Configuration = Configuration.Builder().build()
    ): Configuration {

        // Set flavor dimensions
        flavorDimensions += configuration.dimensions

        productFlavors {
            if (configuration.dimensions.contains(ENVIRONMENT_DIMENSION)) {
                configuration.environments.forEach { env ->
                    create(env.id) {
                        dimension = ENVIRONMENT_DIMENSION

                        configuration.envApplicationIds[env]?.let {
                            applicationId = it
                        }

                        configuration.envApplicationIdSuffixes[env]?.let {
                            applicationIdSuffix = it
                        }

                        configuration.matchingFallbacks[env]?.let {
                            matchingFallbacks.add(it)
                        }
                    }
                }
            }
        }

        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }
            getByName("release") {
                isMinifyEnabled = false
                //TODO define RELEASE CONFIGURATION AND SIGNING !!!!!
//                initWith(getByName(BuildType.RELEASE.value))
//                matchingFallbacks += listOf(BuildType.RELEASE.value)
//                isMinifyEnabled = true
                //TODO proguard
            }
        }

        return configuration
    }
}