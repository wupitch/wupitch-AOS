package wupitch.android.presentation.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.main.home.create_crew.CreateCrewViewModel
import wupitch.android.presentation.ui.main.impromptu.create_impromptu.CreateImprtViewModel
import wupitch.android.presentation.ui.main.my_page.MyPageViewModel


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
                                when(viewModel){
                                    is CreateCrewViewModel -> {
                                        viewModel.setIsUsingDefaultImage(false)
                                    }
                                    is CreateImprtViewModel-> {
                                        viewModel.setIsUsingDefaultImage(false)
                                    }
                                    is MyPageViewModel-> {
                                        viewModel.setIsUsingDefaultImage(false)
                                    }
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
                                when(viewModel){
                                    is CreateCrewViewModel -> {
                                        viewModel.setIsUsingDefaultImage(true)
                                        viewModel.setImageChosenState(true)
                                        viewModel.setCrewImage(Constants.EMPTY_IMAGE_URI)
                                    }
                                    is CreateImprtViewModel-> {
                                        viewModel.setIsUsingDefaultImage(true)
                                        viewModel.setImageChosenState(true)
                                        viewModel.setImprtImage(Constants.EMPTY_IMAGE_URI)
                                    }
                                    is MyPageViewModel-> {
                                        viewModel.setIsUsingDefaultImage(true)
                                        viewModel.setImageChosenState(true)
                                    }
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
                    }
                }
            }
        }
    }

}