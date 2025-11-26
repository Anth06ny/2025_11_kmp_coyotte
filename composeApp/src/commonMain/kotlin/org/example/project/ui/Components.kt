package org.example.project.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch


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
expect fun PictureGallery(modifier:Modifier = Modifier, urlList: List<String>)

@Composable
fun LocationPermissionButton(modifier:Modifier = Modifier, onPermissionResult: (PermissionState) -> Unit = {},) {

    //Outils de demande de permission
    val permissionFactory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionController: PermissionsController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val coroutineScope = rememberCoroutineScope()
    //Un LaunchedEffect qui permet de lier le permissionController au cycle de compose
    BindEffect(permissionController)

    Button(modifier = modifier,
        onClick = {
            //coroutine car la demande de permission est synchrone
            coroutineScope.launch {
                try {
                    //Déclanche la Popup
                    permissionController.providePermission(Permission.LOCATION)
                    onPermissionResult(PermissionState.Granted)
                }
                catch (e: DeniedAlwaysException) {
                    onPermissionResult(PermissionState.DeniedAlways)
                }
                catch (e: DeniedException) {
                    onPermissionResult(PermissionState.Denied)
                }
                catch (e: RequestCanceledException) {
                    e.printStackTrace()
                }
            }
        }) {
        Text(text = "Ask permission")
    }
}