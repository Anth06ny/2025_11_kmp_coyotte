package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.ui.AppNavigation
import org.example.project.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {


    AppTheme {
        Scaffold {
            AppNavigation(modifier = Modifier.padding(it))
        }
    }
}