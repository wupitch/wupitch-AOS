package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
class CreateCrewVisitorFeeFragment : Fragment() {

    private val viewModel : CreateCrewViewModel by activityViewModels()

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val feeState = remember { mutableStateOf(viewModel.crewVisitorFee.value) }
                    val noFeeState = remember { mutableStateOf(viewModel.noCrewVisitorFee.value) }

                    if(noFeeState.value) feeState.value = ""
                    if(feeState.value.isNotEmpty()) noFeeState.value = false

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if(stopSignupState.value) {
                        findNavController().navigate(R.id.action_createCrewVisitorFeeFragment_to_mainFragment)
                    }
                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_crew_warning))
                    }
                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, topDivider, content, visitorDef, nextBtn) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { dialogOpenState.value = true},
                            textString = R.string.create_crew
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
                                text = stringResource(id = R.string.crew_visitor_fee),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            NumberTextFieldLayout(
                                modifier = Modifier
                                    .width(95.dp)
                                    .height(44.dp),
                                textState = feeState,
                                measureString = stringResource(id = R.string.fee_measure),
                                thousandIndicator = true,
                                hintString = stringResource(id = R.string.fee_hint)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            NoToggleLayout(noFeeState, stringResource(id = R.string.no_visitor))

                        }
                        VisitorDefLayout(modifier = Modifier
                            .constrainAs(visitorDef) {
                                bottom.linkTo(nextBtn.top, margin = 40.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                                end.linkTo(parent.end, margin = 20.dp)
                                width = Dimension.fillToConstraints
                            })
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
                            btnColor = if (feeState.value != "" || noFeeState.value) R.color.main_orange else R.color.gray03,
                            textString = R.string.upload,
                            fontSize = 16.sp
                        ) {
                            if (feeState.value != "" || noFeeState.value) {
                                viewModel.setCrewVisitorFee(feeState.value, noFeeState.value)
                                //todo 크루 아이디 생성 후  디테일 페이지로 이동.
                                val bundle = Bundle().apply { putInt("crewId", 0) }
                                findNavController().navigate(R.id.action_createCrewVisitorFeeFragment_to_crewDetailFragment, bundle)
                            }


                        }
                    }
                }
            }
        }
    }
}