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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
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
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI )
        }
    )

    @Composable
    fun LaunchGallery() {
        Log.d("{LaunchGallery}", "gallery")
        SideEffect {
            launcher.launch("image/*")
        }
    }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Permission(
            permission = listOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionNotAvailableContent = {

                //imageChosenState.value = false
//                Column(Modifier) {
//                    Text("O noes! No Photo Gallery!")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row {
//                        Button(
//                            modifier = Modifier.padding(4.dp),
//                            onClick = {
//                                context.startActivity(Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                                    data = Uri.fromParts("package", context.packageName, null)
//                                })
//                            }
//                        ) {
//                            Text("Open Settings")
//                        }
//                        // If they don't want to grant permissions, this button will result in going back
//                        Button(
//                            modifier = Modifier.padding(4.dp),
//                            onClick = {
//                                onImageUri(EMPTY_IMAGE_URI)
//                            }
//                        ) {
//                            Text("Use Camera")
//                        }
//                    }
//                }
            },
        ) {
            LaunchGallery()
        }
    } else {
        Permission(
            permission =  listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionNotAvailableContent = {},
        ) {
            LaunchGallery()
        }
    }
}