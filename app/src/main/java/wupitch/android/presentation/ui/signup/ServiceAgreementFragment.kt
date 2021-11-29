package wupitch.android.presentation.ui.signup

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import androidx.navigation.navGraphViewModels
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

    private val viewModel: SignupViewModel by navGraphViewModels(R.id.signup_nav) {defaultViewModelProviderFactory}

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
                        if(viewModel.pushToggleState.value != null) dialogOpenState.value = true
                        else findNavController().navigateUp()
                    }
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, titleTop, titleBottom, subtitle, allToggleBtn, grayCol, nextBtn)  = createRefs()

                        val allToggleState = remember { mutableStateOf(viewModel.allToggleState.value) }
                        val serviceToggleState = remember { mutableStateOf(viewModel.serviceToggleState.value) }
                        val privacyToggleState = remember { mutableStateOf(viewModel.privacyToggleState.value) }
                        val pushToggleState = remember { mutableStateOf(viewModel.pushToggleState.value ?: false) }

                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            if(viewModel.pushToggleState.value != null) dialogOpenState.value = true
                            else findNavController().navigateUp()
                        })

                        Row(modifier = Modifier.constrainAs(titleTop) {
                            top.linkTo(toolbar.bottom, margin = 24.dp)
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
                                    openBrowser("https://candle-mulberry-ea5.notion.site/264733a76f5f43eeb396442acc96b600")
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
                                    openBrowser("https://candle-mulberry-ea5.notion.site/a75a7eaaaa5e4ff7b665df903cfe6095")
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
                                onDetailClick = null
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
                                viewModel.setPushToggleState(pushToggleState.value)
                                viewModel.setAllToggleState(allToggleState.value)
                                viewModel.setServiceToggleState(serviceToggleState.value)
                                viewModel.setPrivacyToggleState(privacyToggleState.value)

                                findNavController().navigate(R.id.action_serviceAgreementFragment_to_emailPwFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }
}