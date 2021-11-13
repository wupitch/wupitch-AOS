package wupitch.android.presentation.ui.components

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.main.home.create_crew.CreateCrewViewModel


class UploadImageBottomSheetFragment(
    val viewModel : ViewModel
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                WupitchTheme {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(17.dp))

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if(viewModel is CreateCrewViewModel) {
                                    viewModel.setImageSource(false)
                                    viewModel.setImageChosenState(true)
                                }
                                dismiss()
                            }){
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp),
                                text = stringResource(id = R.string.from_gallery),
                                color = colorResource(id = R.color.main_black),
                                fontWeight = FontWeight.Normal,
                                fontFamily = Roboto,
                                fontSize = 16.sp
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if(viewModel is CreateCrewViewModel) {
                                    viewModel.setImageSource(true)
                                    viewModel.setImageChosenState(true)
                                }
                                dismiss()

                            }){
                            Text(
                                modifier = Modifier .padding(top = 16.dp, bottom = 15.dp),
                                text = stringResource(id = R.string.use_default_image),
                                color = colorResource(id = R.color.main_black),
                                fontWeight = FontWeight.Normal,
                                fontFamily = Roboto,
                                fontSize = 16.sp
                            )
                        }
//                        ClickableText(
//                            modifier = Modifier.fillMaxWidth(),
//                            text = AnnotatedString(
//                            stringResource(id = R.string.from_gallery),
//                            spanStyle = SpanStyle(
//                                color = colorResource(id = R.color.main_black),
//                                fontWeight = FontWeight.Normal,
//                                fontFamily = Roboto,
//                                fontSize = 16.sp,
//                            )
//                        ),
//                            onClick = {
//                                Toast.makeText(requireContext(), "hello", Toast.LENGTH_SHORT).show()
//                        } )

                    }
                }
            }
        }
    }

}