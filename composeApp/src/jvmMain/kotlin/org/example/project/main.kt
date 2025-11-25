package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.di.initKoin

fun main() = application {

    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}