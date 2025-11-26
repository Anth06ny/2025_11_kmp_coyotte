package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
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

@Composable
actual fun LocationPermissionButton(modifier: Modifier, onPermissionResult: (MyPermissionState) -> Unit) {

    //Outils de demande de permission
    val permissionFactory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionController: PermissionsController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val coroutineScope = rememberCoroutineScope()
    //Un LaunchedEffect qui permet de lier le permissionController au cycle de compose
    BindEffect(permissionController)

    Button(
        modifier = modifier,
        onClick = {
            //coroutine car la demande de permission est synchrone
            coroutineScope.launch {
                try {
                    //Déclanche la Popup
                    permissionController.providePermission(Permission.LOCATION)
                    onPermissionResult(MyPermissionState.Granted)
                }
                catch (e: DeniedAlwaysException) {
                    onPermissionResult(MyPermissionState.DeniedAlways)
                }
                catch (e: DeniedException) {
                    onPermissionResult(MyPermissionState.Denied)
                }
                catch (e: RequestCanceledException) {
                    e.printStackTrace()
                }
            }
        }) {
        Text(text = "Ask permission")
    }
}