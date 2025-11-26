package org.example.project.service

data class Location(val latitude: Double, val longitude: Double)
//Copier coller de celui de Moko
enum class MyPermissionState {
    /**
     * Starting state for each permission.
     */
    NotDetermined,

    /**
     * Android-only. This could mean [NotDetermined] or [DeniedAlways], but the OS doesn't
     * expose which of the two it is in all scenarios.
     */
    NotGranted,
    Granted,

    /**
     * Android-only.
     */
    Denied,

    /**
     * On Android only applicable to Push Notifications.
     */
    DeniedAlways
}

expect class LocationService() {

    fun getCurrentLocation(gotLocation: (Location?) -> Unit)
}