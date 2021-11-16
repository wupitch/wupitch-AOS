package wupitch.android.presentation.ui.signup.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.Permission

@RequiresApi(Build.VERSION_CODES.Q)
@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@Composable
fun CameraCapture(
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { }
) {
    val context = LocalContext.current

    Permission(
        permission = Manifest.permission.CAMERA,
        permissionNotAvailableContent = {},
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.main_black))
        ) {

            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }

            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()
                )
            }
            val (cameraPreview, button, guideButton, leftDown, leftUp, rightUp, rightDown) = createRefs()

            CameraPreview(
                modifier = Modifier
                    .height(236.dp)
                    .fillMaxWidth()
                    .constrainAs(cameraPreview) {
                        top.linkTo(parent.top)
                        bottom.linkTo(guideButton.top)
                    },
                onUseCase = {
                    previewUseCase = it
                }
            )

            Image(
                modifier = Modifier.constrainAs(leftDown){
                    bottom.linkTo(cameraPreview.bottom, margin = 22.dp)
                    start.linkTo(cameraPreview.start, margin = 40.dp)
                },
                painter = painterResource(id = R.drawable.union), contentDescription = null
            )
            Image(
                modifier = Modifier.constrainAs(leftUp){
                    top.linkTo(cameraPreview.top, margin = 22.dp)
                    start.linkTo(cameraPreview.start, margin = 40.dp)
                },
                painter = painterResource(id = R.drawable.union_2), contentDescription = null
            )
            Image(
                modifier = Modifier.constrainAs(rightUp){
                    top.linkTo(cameraPreview.top, margin = 22.dp)
                    end.linkTo(cameraPreview.end, margin = 40.dp)
                },
                painter = painterResource(id = R.drawable.union_3), contentDescription = null
            )
            Image(
                modifier = Modifier.constrainAs(rightDown){
                    bottom.linkTo(cameraPreview.bottom, margin = 22.dp)
                    end.linkTo(cameraPreview.end, margin = 40.dp)
                },
                painter = painterResource(id = R.drawable.union_4), contentDescription = null
            )
            Button(
                modifier = Modifier
                    .constrainAs(guideButton) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 46.dp)
                    }
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = null,
                onClick = {
                }
            ) {
                Text(
                    text = "",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.Transparent
                )
            }
            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(72.dp)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom, margin = 32.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                elevation = null,
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.gray04)),
                onClick = {
                    coroutineScope.launch {
                        imageCaptureUseCase.takePicture(context.executor).let {
                            onImageFile(it)
                        }
                    }
                }
            ) {}


            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
    }
}
