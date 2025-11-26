package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.service.MyPermissionState
import org.example.project.ui.screens.ImageCard

@Composable
actual fun PictureGallery(modifier: Modifier, urlList: List<String>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(urlList.size) {
            ImageCard(urlList[it])
        }
    }
}

@Composable
actual fun LocationPermissionButton(modifier: Modifier, onPermissionResult: (MyPermissionState) -> Unit) {

    //Pas besoin dpécialement de bouton car on a forcement la permission
    onPermissionResult(MyPermissionState.Granted)
}