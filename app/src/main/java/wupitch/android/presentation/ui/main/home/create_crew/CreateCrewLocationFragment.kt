package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

@AndroidEntryPoint
class CreateCrewLocationFragment : Fragment() {

    private lateinit var districtBottomSheet: DistrictBottomSheetFragment
    private lateinit var dongBottomSheet: DongBottomSheetFragment
    private val viewModel: CreateCrewViewModel by viewModels()

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val locationSelectedState = remember {
                        mutableStateOf(-1)
                    }

                    val textState = remember {
                        mutableStateOf("")
                    }
                    val districtList = viewModel.districtList.value
                    val districtState = viewModel.userDistrictName.observeAsState()
                    //todo 동 추가.


                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, divider, content, nextBtn, progressBar) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { },
                            textString = R.string.create_crew
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(divider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        // todo 동 추가
                        if (districtList.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressBar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(nextBtn.top)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }

                        //todo 동 추가
                        if (districtList.data.isNotEmpty()) {

                            Column(
                                Modifier
                                    .constrainAs(content) {
                                        top.linkTo(divider.bottom, margin = 24.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .padding(horizontal = 20.dp)) {
                                Text(
                                    text = stringResource(id = R.string.location_of_crew),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.main_black),
                                    fontSize = 20.sp,
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                ) {
                                    WhiteRoundBtn(
                                        modifier = Modifier
                                            .height(48.dp)
                                            .width(152.dp),
                                        textString = if (districtState.value != null) districtState.value!! else stringResource(id = R.string.select_region_btn),
                                        fontSize = 14.sp,
                                        textColor = if (districtState.value != null) R.color.main_orange else R.color.gray02,
                                        borderColor = if (districtState.value != null) R.color.main_orange else R.color.gray02
                                    ) {
                                        showDistrictBottomSheet(districtList.data)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    WhiteRoundBtn(
                                        modifier = Modifier
                                            .height(48.dp)
                                            .width(152.dp),
                                        textString = stringResource(id = R.string.select_dong),
                                        fontSize = 14.sp,
                                        textColor = R.color.gray02,
                                        borderColor = R.color.gray02
                                    ) {
//                                        showRegionBottomSheet(list)
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = stringResource(id = R.string.location),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.main_black),
                                    fontSize = 16.sp,
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                LocationTextField(textState)



                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = R.string.empty_location_warning),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(id = R.color.gray02),
                                    fontSize = 14.sp,
                                )


                            }
                        }




                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom, margin = 24.dp)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor = if (districtState.value != null) R.color.main_orange else R.color.gray03,
                            textString = R.string.two_over_seven,
                            fontSize = 16.sp
                        ) {
                            if (districtState.value != null) {
                                Log.d("{CreateCrewLocationFragment.onCreateView}", "next btn clicked!")
                                //todo viewmodel 에 선택된 location 보내기.
                                findNavController().navigate(R.id.action_createCrewLocationFragment_to_createCrewInfoFragment)
                            }
                        }


                    }
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    private fun LocationTextField(
        textState: MutableState<String>
    ) {
        BasicTextField(
            value = textState.value,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            onValueChange = { value ->
                textState.value = value
            },
            cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.gray04))
                        .padding(horizontal = 16.dp)
                        .padding(top = 11.dp, bottom = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        val (hint) = createRefs()

                        innerTextField()
                        if (textState.value.isEmpty()) {
                            Text(
                                modifier = Modifier.constrainAs(hint) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                text = stringResource(id = R.string.input_location),
                                color = colorResource(id = R.color.gray03),
                                fontSize = 16.sp,
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDistricts()
    }

    private fun showDistrictBottomSheet(districtList: Array<String>) {
        districtBottomSheet = DistrictBottomSheetFragment(districtList, viewModel)
        districtBottomSheet.show(childFragmentManager, "district_bottom_sheet")
    }

    private fun showDongBottomSheet(districtList: Array<String>) {
        //todo district list -> dong list.
       dongBottomSheet = DongBottomSheetFragment(districtList, viewModel)
       dongBottomSheet.show(childFragmentManager, "dong_bottom_sheet")
    }
}