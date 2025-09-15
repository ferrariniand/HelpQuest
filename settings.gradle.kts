rootProject.name = "HelpQuest"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
//TODO add modules
//include(":core:domain")
//include(":core:data")
//include(":core:database")
//include(":core:presentation:designsystem")
//include(":core:presentation:ui")
//include(":auth:domain")
//include(":auth:data")
//include(":auth:presentation")
//include(":profile:domain")
//include(":profile:data")
//include(":profile:presentation")
//include(":messaging:domain")
//include(":messaging:data")
//include(":messaging:presentation")
//include(":settings:domain")
//include(":settings:data")
//include(":settings:presentation")
//include(":quests:domain")
//include(":quests:data")
//include(":quests:presentation")
//include(":home:domain")
//include(":home:presentation")
