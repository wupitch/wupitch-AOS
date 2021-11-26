package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.WhiteRoundBtn
import wupitch.android.presentation.ui.components.DistrictBottomSheetFragment
import wupitch.android.presentation.ui.components.StopWarningDialog

@AndroidEntryPoint
class MyPageDistrictFragment : Fragment() {

    private lateinit var districtBottomSheet: DistrictBottomSheetFragment
    val viewModel: MyPageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val districtList = viewModel.districtList.value
                    val updateState = remember { viewModel.updateState }
                    if (updateState.value.isSuccess) findNavController().navigateUp()
                    if (updateState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            updateState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (districtList.data.isNotEmpty()) {


                        ConstraintLayout(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize()
                        ) {
                            val (toolbar, title, districtBtn, nextBtn, progressbar) = createRefs()
                            val regionState = viewModel.userDistrictName.observeAsState()

                            IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }, onLeftIconClick = { findNavController().navigateUp() })

                            Text(
                                modifier = Modifier.constrainAs(title) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(toolbar.bottom, margin = 24.dp)
                                },
                                text = stringResource(id = R.string.select_region),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                lineHeight = 32.sp
                            )

                            WhiteRoundBtn(
                                modifier = Modifier
                                    .constrainAs(districtBtn) {
                                        start.linkTo(title.start)
                                        top.linkTo(title.bottom, margin = 32.dp)
                                    }
                                    .height(48.dp)
                                    .width(152.dp),
                                textString = if (regionState.value != null) regionState.value!! else stringResource(
                                    id = R.string.select_region_btn
                                ),
                                fontSize = 14.sp,
                                textColor = if (regionState.value != null) R.color.main_orange else R.color.gray02,
                                borderColor = if (regionState.value != null) R.color.main_orange else R.color.gray02
                            ) {
                                showRegionBottomSheet(districtList.data)
                            }

                            if (districtList.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.constrainAs(progressbar) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        top.linkTo(toolbar.bottom)
                                        bottom.linkTo(nextBtn.top)
                                    },
                                    color = colorResource(id = R.color.main_orange)
                                )
                            }

                            RoundBtn(
                                modifier = Modifier
                                    .constrainAs(nextBtn) {
                                        bottom.linkTo(parent.bottom, margin = 32.dp)
                                        start.linkTo(parent.start, margin = 20.dp)
                                        end.linkTo(parent.end, margin = 20.dp)
                                        width = Dimension.fillToConstraints
                                    }
                                    .fillMaxWidth()
                                    .height(52.dp),
                                btnColor = if (regionState.value != null) R.color.main_orange else R.color.gray03,
                                textString = R.string.done,
                                fontSize = 16.sp
                            ) {
                                if (regionState.value != null) {
                                    viewModel.changeUserDistrict()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDistricts()
    }


    private fun showRegionBottomSheet(districtList: Array<String>) {
        districtBottomSheet = DistrictBottomSheetFragment(districtList, viewModel)
        districtBottomSheet.show(childFragmentManager, "region_bottom_sheet")
    }
}