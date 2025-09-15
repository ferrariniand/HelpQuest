import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Coil
            implementation(libs.coil.compose)

            // Core
            implementation(libs.androidx.core.ktx)

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

            // Splash screen
            implementation(libs.core.splashscreen)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.compose)

//            implementation(projects.core.domain)
//            implementation(projects.core.database)
//            implementation(projects.core.data)
//            implementation(projects.core.presentation.designsystem)
//            implementation(projects.core.presentation.ui)
//
//            implementation(projects.auth.domain)
//            implementation(projects.auth.data)
//            implementation(projects.auth.presentation)
//
//            implementation(projects.profile.domain)
//            implementation(projects.profile.data)
//            implementation(projects.profile.presentation)
//
//            implementation(projects.quests.domain)
//            implementation(projects.quests.data)
//            implementation(projects.quests.presentation)
//
//            implementation(projects.messaging.domain)
//            implementation(projects.messaging.data)
//            implementation(projects.messaging.presentation)
//
//            implementation(projects.settings.domain)
//            implementation(projects.settings.data)
//            implementation(projects.settings.presentation)
//
//            implementation(projects.home.domain)
//            implementation(projects.home.presentation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.helpquest"
    compileSdk = libs.versions.projectCompileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.helpquest"
        minSdk = libs.versions.projectMinSdkVersion.get().toInt()
        targetSdk = libs.versions.projectTargetSdkVersion.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.helpquest.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.helpquest"
            packageVersion = "1.0.0"
        }
    }
}
