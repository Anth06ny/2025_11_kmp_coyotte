import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    kotlin("plugin.serialization") version "2.1.0"
}

// Read API key from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val photographerAPIKey: String? = localProperties.getProperty("photographer.api.key")


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            if (photographerAPIKey != null) {
                // Pass the API key to the Kotlin/Native compiler to be added to the Info.plist
                freeCompilerArgs += "-Xbinary=PHOTOGRAPHER_API_KEY=$photographerAPIKey"
            }
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)


            //Client de requêtes spécifique à Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")

            //Si besoin du context
            implementation("io.insert-koin:koin-android:4.1.+")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.+")


            // (les interfaces en gros)
            implementation("io.ktor:ktor-client-core:3.2.2")
            //Intégration avec la bibliothèque de serialisation, gestion des headers
            implementation("io.ktor:ktor-client-content-negotiation:3.2.2")
            //Serialisation JSON
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.2")
            //Pour le logger
            implementation("io.ktor:ktor-client-logging:3.2.2")

            //Coil ImageLoader
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.+")

            //Injection dépendance KOIN
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            implementation("io.insert-koin:koin-test:4.1.+")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            //Client de requêtes spécifique au bureau sur JVM donc même qu'Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")
        }
        iosMain.dependencies {
            //Client de requêtes spécifique à iOS
            implementation("io.ktor:ktor-client-darwin:3.2.2")
        }

    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        //Ajoute une constante dans la class BuildConfig généré par Gradle
        buildConfigField("String", "PHOTOGRAPHER_API_KEY", "\"${photographerAPIKey ?: ""}\"")
    }
    //Génère le fichier BuildConfig
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        //Injecte la clé d'API dans une variable d'environnement à l'appel du programme
        if (localProperties.containsKey("photographer.api.key")) {
            jvmArgs += "-Dphotographer.api.key=$photographerAPIKey"
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}
