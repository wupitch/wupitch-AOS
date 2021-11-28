package wupitch.android.presentation.ui.main.impromptu.create_impromptu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

@AndroidEntryPoint
class CreateImprtSizeFragment : Fragment() {

    private lateinit var recruitSizeBottomSheet: RecruitSizeBottomSheetFragment
    private val viewModel: CreateImprtViewModel by activityViewModels()

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val sizeState = remember { viewModel.imprtSize }

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if (stopSignupState.value) {
                        val bundle = Bundle().apply { putInt("tabId", R.id.impromptuFragment) }
                        findNavController().navigate(R.id.action_createImprtSizeFragment_to_mainFragment, bundle)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_impromptu_warning)
                        )
                    }

                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, topDivider, content, nextBtn) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { dialogOpenState.value = true },
                            textString = R.string.create_impromptu
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        Column(modifier = Modifier
                            .constrainAs(content) {
                                top.linkTo(topDivider.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(nextBtn.top)
                                height = Dimension.fillToConstraints
                                width = Dimension.fillToConstraints
                            }
                            .padding(horizontal = 20.dp))
                        {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = stringResource(id = R.string.title_num_of_recruitment),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            SizeLayout(sizeState)

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
                            btnColor = if (sizeState.value != "00") R.color.main_orange else R.color.gray03,
                            textString = R.string.four_over_five,
                            fontSize = 16.sp
                        ) {
                            if (sizeState.value != "00") {
                                findNavController().navigate(R.id.action_createImprtSizeFragment_to_createImprtFeeFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SizeLayout(
        sizeTextState: State<String>
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .width(55.dp)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        colorResource(id = R.color.gray04)
                    ).clickable {
                        showRecruitSizeBottomSheet()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sizeTextState.value,
                    fontFamily = Roboto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = if(sizeTextState.value == "00") colorResource(id = R.color.gray03) else Color.Black
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.people_count_measure),
                fontFamily = Roboto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )


        }
    }

    private fun showRecruitSizeBottomSheet() {
        recruitSizeBottomSheet = RecruitSizeBottomSheetFragment(viewModel)
        recruitSizeBottomSheet.show(childFragmentManager, "district_bottom_sheet")
    }


}