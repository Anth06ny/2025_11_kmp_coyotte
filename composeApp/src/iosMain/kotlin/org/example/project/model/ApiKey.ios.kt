package org.example.project.model

import platform.Foundation.NSBundle

internal actual fun getPhotographerAPIKey(): String {
    // Retrieve the API key from the app's Info.plist file
    // This value is inserted by Gradle during the build process.
    val apiKey = NSBundle.mainBundle.objectForInfoDictionaryKey("PHOTOGRAPHER_API_KEY") as? String
    if (apiKey.isNullOrBlank()) {
        println("WARNING: API key not found in Info.plist. Make sure it's set in the Gradle build.")
        return ""
    }
    return apiKey
}