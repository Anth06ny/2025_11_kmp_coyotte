package org.example.project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.toto
import org.example.project.model.Photographer
import org.example.project.ui.LocationPermissionButton
import org.example.project.ui.MyError
import org.example.project.ui.MyPermissionState
import org.example.project.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource


//Composable représentant l'ensemble de l'écran
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier, mainViewModel: MainViewModel,
    onPictureRowClick: (Photographer) -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var searchText = remember { mutableStateOf("") }
        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value.filter { it.stageName.contains(searchText.value, true) }
        val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()
        val runInProgress by mainViewModel.runInProgress.collectAsStateWithLifecycle()

        var permissionState by remember { mutableStateOf(MyPermissionState.NotDetermined) }
        val location by mainViewModel.location.collectAsStateWithLifecycle()

        SearchBar(searchText = searchText)

        MyError(errorMessage = errorMessage)

        LaunchedEffect(Unit) {
            mainViewModel.updateLocation()
        }

        AnimatedVisibility(visible = runInProgress) {
            CircularProgressIndicator()
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list.size) {
                PictureRowItem(
                    data = list[it],
                    onPictureClick = { onPictureRowClick(list[it]) }
                )
            }
        }

        Row {

            Button(
                onClick = { searchText.value = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clear filter")
            }
            Button(
                onClick = { /* Do something! */ },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Load data")
            }

            LocationPermissionButton { permissionResult ->
                permissionState = permissionResult
                //J'ai la permission j'update la location
                mainViewModel.updateLocation()
            }
        }
        Text(text = "Permission : ${permissionState.name}")
        Text(text = "Location : $location")
    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: MutableState<String>) {

    TextField(
        value = searchText.value,
        onValueChange = { searchText.value = it }, //Action
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        placeholder = { //Texte d'aide
            Text("Votre recherche ici")
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)//Hauteur minimum
            .padding(8.dp)
    )
}

@Composable
fun PictureRowItem(modifier: Modifier = Modifier, data: Photographer, onPictureClick: () -> Unit) {


    var expended by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        AsyncImage(
            model = data.photoUrl,
            contentDescription = data.stageName,
            contentScale = ContentScale.FillWidth,
            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(Res.drawable.toto),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),
            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
                .clickable(onClick = onPictureClick)
        )

        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    expended = !expended
                }
        ) {
            Text(
                text = data.stageName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (expended) data.story else (data.story.take(20) + "..."),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}