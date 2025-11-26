package org.example.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.model.PhotographerAPI
import org.example.project.model.databaseModule
import org.example.project.service.LocationService
import org.example.project.viewmodel.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre au lancement de Koin
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule, databaseModule(), viewModelModule, locationModule)
    }.koin

// Version pour iOS et Desktop
fun initKoin() = initKoin {}

//------------------------
//DECLARATION DES MODULES
//------------------------
val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }
    }

    //Création d'un singleton pour les repository.
    //Get() injectera les objets déjà connues par koin, ici le HttpClient
    //single { PhotographerAPI(get()) }

    //Version avec injection automatique des objets connues
    singleOf(::PhotographerAPI)

}

val locationModule = module {
    singleOf(::LocationService)
}

//Version spécifique au ViewModel
val viewModelModule = module {

    viewModelOf(::MainViewModel)
}