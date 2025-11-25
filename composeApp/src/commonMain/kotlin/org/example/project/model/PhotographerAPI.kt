package org.example.project.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import org.example.project.di.initKoin


//Dans commonMain/model
suspend fun main() {
    val koin = initKoin()
    val photographerAPI = koin.get<PhotographerAPI>()
    println(photographerAPI.loadPhotographers().joinToString(separator = "\n\n"))
}

class PhotographerAPI(val client: HttpClient ) {
    private val API_URL =
        "https://www.amonteiro.fr/api/photographers?apikey=${getPhotographerAPIKey()}"


    //GET
    suspend fun loadPhotographers(): List<Photographer> {
        val response = client.get(API_URL) {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }

    fun loadPhotographersFlow() = flow<List<Photographer>> {
        emit(client.get(API_URL).body())
    }
}

@Serializable
data class Photographer(
    var id: Int,
    val stageName: String,
    val photoUrl: String,
    val story: String,
    val portfolio: List<String>
)
