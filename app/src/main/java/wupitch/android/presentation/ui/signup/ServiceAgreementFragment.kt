package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.signup.components.AllToggleIcon
import wupitch.android.presentation.ui.components.StopWarningDialog
import wupitch.android.presentation.ui.signup.components.ToggleIcon

@AndroidEntryPoint
class ServiceAgreementFragment : Fragment() {

    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if(stopSignupState.value) { findNavController().navigateUp() }

                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.warning_stop_signup))
                    }
                    BackHandler {
                        if(viewModel.userPushAgreed.value != null) dialogOpenState.value = true
                        else findNavController().navigateUp()
                    }
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, titleTop, titleBottom, subtitle, allToggleBtn, grayCol, nextBtn)  = createRefs()

                        val allToggleState = remember { mutableStateOf(false) }
                        val serviceToggleState = remember { mutableStateOf(false) }
                        val privacyToggleState = remember { mutableStateOf(false) }
                        val pushToggleState = remember { mutableStateOf(false) }

                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            if(viewModel.userPushAgreed.value != null) dialogOpenState.value = true
                            else findNavController().navigateUp()
                        })

                        Row(modifier = Modifier.constrainAs(titleTop) {
                            top.linkTo(toolbar.bottom, margin = 32.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                        }) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_orange),
                                fontSize = 22.sp
                            )
                            Text(
                                text = stringResource(id = R.string.service_agreement_title_top),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 22.sp
                            )
                        }

                        Text(
                            modifier = Modifier.constrainAs(titleBottom) {
                                top.linkTo(titleTop.bottom, margin = 4.dp)
                                start.linkTo(titleTop.start)
                            },
                            text = stringResource(id = R.string.service_agreement_title_bottom),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp
                        )
                        Text(
                            modifier = Modifier.constrainAs(subtitle) {
                                top.linkTo(titleBottom.bottom, margin = 7.dp)
                                start.linkTo(titleBottom.start)
                            },
                            text = stringResource(id = R.string.service_agreement_subtitle),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.gray02),
                            fontSize = 14.sp
                        )

                        AllToggleIcon(
                            toggleState = allToggleState,
                            modifier = Modifier
                                .constrainAs(allToggleBtn) {
                                    start.linkTo(subtitle.start)
                                    top.linkTo(subtitle.bottom, margin = 58.dp)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                },
                            onToggleClick = {
                                if(!allToggleState.value) {
                                    privacyToggleState.value = true
                                    serviceToggleState.value = true
                                    pushToggleState.value = true
                                    allToggleState.value = true
                                }else {
                                    privacyToggleState.value = false
                                    serviceToggleState.value = false
                                    pushToggleState.value = false
                                    allToggleState.value = false
                                }
                            },
                            onCheckedChange = {},
                            textString = R.string.agree_all,
                            onDetailClick = null
                        )

                        Column(
                            modifier = Modifier
                                .constrainAs(grayCol) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(allToggleBtn.bottom, margin = 16.dp)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                }
                                .background(colorResource(id = R.color.gray04))
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp)
                        ) {

                            ToggleIcon(
                                toggleState = serviceToggleState,
                                modifier = Modifier.fillMaxWidth(),
                                onCheckedChange = {
                                    serviceToggleState.value = it
                                    if(!it && allToggleState.value) allToggleState.value = false
                                    if(serviceToggleState.value && privacyToggleState.value && pushToggleState.value) {
                                        allToggleState.value = true
                                    }
                                },
                                textString = R.string.terms_of_service_agreement,
                                onDetailClick = {
                                    findNavController().navigate(R.id.action_serviceAgreementFragment_to_serviceAgreementDetailFragment)
                                }
                            )

                            ToggleIcon(
                                toggleState = privacyToggleState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 19.dp),
                                onCheckedChange = {
                                    privacyToggleState.value = it
                                    if(!it && allToggleState.value) allToggleState.value = false
                                    if(serviceToggleState.value && privacyToggleState.value && pushToggleState.value) {
                                        allToggleState.value = true
                                    }
                                },
                                textString = R.string.terms_of_privacy_policy,
                                onDetailClick = {
                                    findNavController().navigate(R.id.action_serviceAgreementFragment_to_useOfPersonalInfoDetailFragment)
                                }
                            )

                            ToggleIcon(
                                toggleState = pushToggleState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 19.dp),
                                onCheckedChange = {
                                    pushToggleState.value = it
                                    if(!it && allToggleState.value) allToggleState.value = false
                                    if(serviceToggleState.value && privacyToggleState.value && pushToggleState.value) {
                                        allToggleState.value = true
                                    }
                                },
                                textString = R.string.push_notification_agreement,
                                onDetailClick = {
                                    //todo 푸시 알림 동의 약관 페이지로 이동!!!
                                }
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
                            btnColor = if(serviceToggleState.value && privacyToggleState.value) R.color.main_orange else R.color.gray03,
                            textString = R.string.next_one_over_four,
                            fontSize = 16.sp
                        ) {
                            if(serviceToggleState.value && privacyToggleState.value){
                                viewModel.setUserPushAgreement(pushToggleState.value)
                                findNavController().navigate(R.id.action_serviceAgreementFragment_to_emailPwFragment)
                            }
                        }
                    }
                }
            }
        }
    }
}