plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.helpquest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.helpquest"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    //api command is like implementation command, but extends the usage of the lib to the modules that depends on this module
    api(libs.play.feature.delivery)
    api(libs.play.feature.delivery.ktx)
    api(libs.play.review)
    api(libs.play.review.ktx)
    api(libs.play.app.update)
    api(libs.play.app.update.ktx)
    api(libs.play.asset.delivery)
    api(libs.play.asset.delivery.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.data)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.presentation)

    implementation(projects.profile.domain)
    implementation(projects.profile.data)
    implementation(projects.profile.presentation)

    implementation(projects.quests.domain)
    implementation(projects.quests.data)
    implementation(projects.quests.presentation)

    implementation(projects.messaging.domain)
    implementation(projects.messaging.data)
    implementation(projects.messaging.presentation)

    implementation(projects.settings.domain)
    implementation(projects.settings.data)
    implementation(projects.settings.presentation)

    implementation(projects.home.domain)
    implementation(projects.home.presentation)
}