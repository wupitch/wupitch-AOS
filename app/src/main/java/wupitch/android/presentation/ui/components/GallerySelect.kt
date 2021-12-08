package wupitch.android.presentation.ui.components

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import wupitch.android.common.Constants.EMPTY_IMAGE_URI

@ExperimentalPermissionsApi
@Composable
fun GallerySelect(
    onImageUri: (Uri) -> Unit = { }
) {

    val context = LocalContext.current

    val settingDialogState = remember { mutableStateOf(false)}
    if(settingDialogState.value) {
        NoGalleryPermissionDialog(settingDialogState){
            context.startActivity(Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            })
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI )
        }
    )

    @Composable
    fun LaunchGallery() {
        SideEffect {
            launcher.launch("image/*")
        }
    }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Permission(
            permission = listOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionNotAvailableContent = {
                settingDialogState.value = true
            },
        ) {
            LaunchGallery()
        }
    } else {
        Permission(
            permission =  listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionNotAvailableContent = {
                settingDialogState.value = true
            },
        ) {
            LaunchGallery()
        }
    }
}