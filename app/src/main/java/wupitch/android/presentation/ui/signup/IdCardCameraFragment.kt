package wupitch.android.presentation.ui.signup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import wupitch.android.R
import wupitch.android.common.Constants.EMPTY_IMAGE_URI
import wupitch.android.presentation.theme.CameraTheme
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.signup.components.CameraCapture

@AndroidEntryPoint
class IdCardCameraFragment : Fragment() {

    private val viewModel : SignupViewModel by navGraphViewModels(R.id.signup_nav) {defaultViewModelProviderFactory}

    @ExperimentalCoroutinesApi
    @ExperimentalPermissionsApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CameraTheme {

                    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
                    val signupState = viewModel.signupState
                    if(signupState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), signupState.value.error, Toast.LENGTH_SHORT).show()
                        viewModel.initSignupState()
                    }
                    if(signupState.value.isSuccess) findNavController().navigate(R.id.action_idCardCameraFragment_to_reqIdCertiFragment)

                    ConstraintLayout(
                        Modifier
                            .background(colorResource(id = R.color.main_black))
                            .fillMaxSize()
                    ) {

                        val (takenImage, retake, useImage, progressbar) = createRefs()

                        if (imageUri != EMPTY_IMAGE_URI) {

                            Image(
                                modifier = Modifier
                                    .height(236.dp)
                                    .constrainAs(takenImage) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(useImage.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    },
                                contentScale = ContentScale.Crop,
                                painter = rememberImagePainter(imageUri),
                                contentDescription = "Captured image"
                            )
                            Button(
                                modifier = Modifier
                                    .constrainAs(retake) {
                                        start.linkTo(parent.start)
                                        bottom.linkTo(parent.bottom, margin = 46.dp)
                                    }
                                    .padding(horizontal = 30.dp, vertical = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = null,
                                onClick = {
                                    imageUri = EMPTY_IMAGE_URI
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.retake),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .constrainAs(useImage) {
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom, margin = 46.dp)
                                    }
                                    .padding(horizontal = 30.dp, vertical = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = null,
                                onClick = {
                                    viewModel.setIdCardImage(imageUri)
                                    viewModel.initFcm()
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.use_photo),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        } else {
                            CameraCapture(
                                onImageFile = { file ->
                                    imageUri = file.toUri()
                                }
                            )
                        }

                        if (signupState.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }
                    }
                }
            }
        }
    }
}
