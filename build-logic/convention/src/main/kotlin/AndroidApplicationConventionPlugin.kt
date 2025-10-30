import com.android.build.api.dsl.ApplicationExtension
import com.helpquest.convention.configureBuildTypes
import com.helpquest.convention.configureKotlinAndroid
import com.helpquest.convention.getProjectApplicationId
import com.helpquest.convention.getProjectTargetSdkVersion
import com.helpquest.convention.getProjectVersionCode
import com.helpquest.convention.getProjectVersionName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                namespace = "com.helpquest"


                defaultConfig {
                    applicationId = getProjectApplicationId()
                    targetSdk = getProjectTargetSdkVersion()
                    versionCode = getProjectVersionCode()
                    versionName = getProjectVersionName()

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        merges += "/META-INF/LICENSE.md"
                        merges += "/META-INF/LICENSE-notice.md"
                    }
                }
                testOptions {
                    packaging {
                        resources {
                            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                            merges += "/META-INF/LICENSE.md"
                            merges += "/META-INF/LICENSE-notice.md"
                        }
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                configureKotlinAndroid(this)
            }

            configureBuildTypes()
        }
    }
}