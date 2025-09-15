pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "HelpQuest"
include(":androidApp")
include(":auth:domain")
include(":auth:data")
include(":auth:presentation")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":profile:domain")
include(":profile:data")
include(":profile:presentation")
include(":messaging:domain")
include(":messaging:data")
include(":messaging:presentation")
include(":settings:domain")
include(":settings:data")
include(":settings:presentation")
include(":quests:domain")
include(":quests:data")
include(":quests:presentation")
include(":home:domain")
include(":home:presentation")
