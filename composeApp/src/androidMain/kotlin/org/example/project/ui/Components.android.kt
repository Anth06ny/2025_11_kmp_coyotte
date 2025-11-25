package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.ui.screens.ImageCard

@Composable
actual fun PictureGallery(modifier: Modifier, urlList: List<String>) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(urlList.size) {
            ImageCard(urlList[it])
        }
    }

}