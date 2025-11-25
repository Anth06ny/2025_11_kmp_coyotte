package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.di.apiModule
import org.example.project.di.viewModelModule
import org.example.project.ui.screens.SearchScreen
import org.example.project.ui.theme.AppTheme
import org.example.project.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

//Code affiché dans la Preview, thème claire, thème sombre
@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
            or android.content.res.Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenPreview() {
    val context = LocalContext.current
    KoinApplicationPreview(application = {
        androidContext(context)
        modules(viewModelModule, apiModule)
    }) {

        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val mainViewModel = koinViewModel<MainViewModel>()
                mainViewModel.loadFakeData(true, "Un message d'erreur")
                SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = mainViewModel)
            }
        }
    }
}