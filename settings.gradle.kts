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

rootProject.name = "Book Cast"
include(":app")
include(":feature:auth")
include(":feature:home")
include(":feature:details")
include(":feature:discover")
include(":feature:search")
include(":core:ui")
include(":feature:favorites")
include(":feature:genres")
include(":feature:currents")
include(":feature:player")
include(":core:data")
include(":core:domain")
include(":core:player")
include(":feature:profile")
