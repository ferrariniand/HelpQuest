rootProject.name = "HelpQuest"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.2"
}

include(":composeApp")
include(":core:domain")
include(":core:database")
include(":core:data")
include(":core:designsystem")
include(":core:presentation")
include(":core:test")
include(":core:mock")
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:auth:presentation")
include(":feature:profile:domain")
include(":feature:profile:data")
include(":feature:profile:presentation")
include(":feature:chat:domain")
include(":feature:chat:data")
include(":feature:chat:presentation")
include(":feature:settings:domain")
include(":feature:settings:data")
include(":feature:settings:presentation")
include(":feature:quests:domain")
include(":feature:quests:data")
include(":feature:quests:presentation")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:chat:database")
