import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.convention.cmp.application)
    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Core
            implementation(libs.androidx.core.ktx)

            // Crypto
            implementation(libs.androidx.security.crypto.ktx)

            // Splash screen
            implementation(libs.core.splashscreen)

            //api command is like implementation command, but extends the usage of the lib to the modules that depends on this module
            api(libs.play.feature.delivery)
            api(libs.play.feature.delivery.ktx)
            api(libs.play.review)
            api(libs.play.review.ktx)
            api(libs.play.app.update)
            api(libs.play.app.update.ktx)
            api(libs.play.asset.delivery)
            api(libs.play.asset.delivery.ktx)

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

            // Coil
            implementation(libs.coil.compose)

            // Core Modules
            implementation(projects.core.domain)
            implementation(projects.core.database)
            implementation(projects.core.data)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            // Auth Modules
            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.data)
            implementation(projects.feature.auth.presentation)
            // Profile Modules
            implementation(projects.feature.profile.domain)
            implementation(projects.feature.profile.data)
            implementation(projects.feature.profile.presentation)
            // Quests Modules
            implementation(projects.feature.quests.domain)
            implementation(projects.feature.quests.data)
            implementation(projects.feature.quests.presentation)
            // Chat Modules
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.database)
            implementation(projects.feature.chat.presentation)
            // Settings Modules
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.data)
            implementation(projects.feature.settings.presentation)
            // Home Modules
            implementation(projects.feature.home.domain)
            implementation(projects.feature.home.presentation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jsystemthemedetector)


            implementation(projects.core.presentation)
        }
    }
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
