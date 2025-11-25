package org.example.project.model

internal actual fun getPhotographerAPIKey(): String {

    // This property is set by Gradle during the `run` task or when building
    val apiKey = System.getProperty("photographer.api.key")
    if (apiKey.isNullOrBlank()) {
        println("WARNING: API key is null or empty. It must be provided as a system property.")
        return ""
    }
    return apiKey
}