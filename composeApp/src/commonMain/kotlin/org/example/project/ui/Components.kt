package org.example.project.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


//Le composant est réutilisable avec n'importe quelle chaine de caractère
@Composable
fun MyError(
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    //permet d'afficher / masquer l'erreur avec une animation
    AnimatedVisibility(!errorMessage.isNullOrBlank()) {
        Text(
            text = errorMessage ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onError,
            modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.error)
        )
    }
}

@Composable
expect fun PictureGallery(modifier: Modifier = Modifier, urlList: List<String>)

@Composable
expect fun LocationPermissionButton(modifier: Modifier = Modifier, onPermissionResult: (MyPermissionState) -> Unit = {})

enum class MyPermissionState {
    /**
     * Starting state for each permission.
     */
    NotDetermined,

    /**
     * Android-only. This could mean [NotDetermined] or [DeniedAlways], but the OS doesn't
     * expose which of the two it is in all scenarios.
     */
    NotGranted,

    Granted,

    /**
     * Android-only.
     */
    Denied,

    /**
     * On Android only applicable to Push Notifications.
     */
    DeniedAlways
}