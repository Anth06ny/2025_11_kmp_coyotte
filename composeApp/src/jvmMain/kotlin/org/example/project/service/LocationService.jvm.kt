package org.example.project.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.project.di.initKoin
import org.koin.java.KoinJavaComponent.inject

suspend fun main() {
    val koin = initKoin()
    val locationService = koin.get<LocationService>()
    println(locationService.getLocationFromIP())

}

actual class LocationService {

    val httpClient: HttpClient by inject(HttpClient::class.java)

    actual fun getCurrentLocation(gotLocation: (Location?) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val location = getLocationFromIP()
                //retour sur le thread principale
                withContext(Dispatchers.Main) {
                    gotLocation(location)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                //retour sur le thread principale
                withContext(Dispatchers.Main) {
                    gotLocation(null)
                }
            }
        }
    }

    suspend fun getLocationFromIP(): Location? {
        return try {
            // ipapi.co - gratuit, pas besoin d'API key
            val response = httpClient.get("https://ipapi.co/json/")
            val json = Json { ignoreUnknownKeys = true }
            val data = json.decodeFromString<IPApiResponse>(response.bodyAsText())
            Location(
                latitude = data.latitude,
                longitude = data.longitude
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Serializable
private data class IPApiResponse(
    val latitude: Double,
    val longitude: Double,
    val city: String? = null,
    val country: String? = null
)