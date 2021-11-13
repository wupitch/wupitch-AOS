package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.StopWarningDialog

class ProfileFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val stopSignupState = remember {
                        mutableStateOf(false)
                    }
                    val dialogOpenState = remember {
                        mutableStateOf(false)
                    }
                    if(stopSignupState.value) {
                        findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                    }
                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                        textString = stringResource(id = R.string.warning_stop_signup))
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(bottom = 32.dp)
                    ) {
                        val (toolbar, title, nicknameEt, introEt, nicknameValidation, introCounter, nextBtn) = createRefs()

                        val nicknameState = remember {
                            mutableStateOf("")
                        }

                        val introState = remember {
                            mutableStateOf("")
                        }

                        val isNicknameValidState = viewModel.isNicknameValid.observeAsState()


                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            findNavController().navigateUp()
                        },
                            hasRightIcon = true,
                            onRightIconClick = {
                                dialogOpenState.value = true
                            }
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(title) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(toolbar.bottom, margin = 32.dp)
                                },
                            text = stringResource(id = R.string.set_profile),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start
                        )

                        ProfileTextField(
                            modifier = Modifier
                                .constrainAs(nicknameEt) {
                                    top.linkTo(title.bottom, margin = 48.dp)
                                    start.linkTo(title.start)
                                }
                                .width(152.dp)
                                .height(44.dp),
                            textString = R.string.input_nickname,
                            stringState = nicknameState,
                            maxLength = 6,
                            validateNickname = true
                        )

                        ProfileTextField(
                            modifier = Modifier
                                .constrainAs(introEt) {
                                    top.linkTo(nicknameEt.bottom, margin = 16.dp)
                                    start.linkTo(nicknameEt.start)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(164.dp),
                            textString = R.string.input_introduction,
                            stringState = introState,
                            maxLength = 100
                        )
                        if (isNicknameValidState.value != null && nicknameState.value.isNotEmpty()) {
                            Text(
                                modifier = Modifier
                                    .constrainAs(nicknameValidation) {
                                        start.linkTo(nicknameEt.end, margin = 15.dp)
                                        bottom.linkTo(nicknameEt.bottom)
                                    },
                                text = if (isNicknameValidState.value!!) stringResource(id = R.string.available_nickname)
                                else stringResource(id = R.string.not_available_nickname),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal,
                                color = if (isNicknameValidState.value!!) colorResource(id = R.color.green)
                                else colorResource(id = R.color.red),
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            modifier = Modifier
                                .constrainAs(introCounter) {
                                    top.linkTo(introEt.bottom, margin = 2.dp)
                                    end.linkTo(introEt.end)
                                },
                            text = "${introState.value.length}/100",
                            color = colorResource(
                                id = R.color.gray03
                            ),
                            fontSize = 12.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor = if (introState.value.isNotEmpty() &&
                                isNicknameValidState.value == true && nicknameState.value.isNotEmpty()
                            ) R.color.main_orange else R.color.gray03,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            if (introState.value.isNotEmpty() &&
                                isNicknameValidState.value == true
                            ) {
                                findNavController().navigate(R.id.action_profileFragment_to_welcomeFragment)
                                //todo : viewmodel 에 닉네임, 소개글 보내기.
                            }
                        }


                    }

                }
            }
        }
    }

    @Composable
    fun ProfileTextField(
        modifier: Modifier,
        @StringRes textString: Int,
        stringState: MutableState<String>,
        maxLength: Int,
        validateNickname: Boolean = false
    ) {

        val customTextSelectionColors = TextSelectionColors(
            handleColor = colorResource(id = R.color.main_orange),
            backgroundColor = colorResource(id = R.color.orange02)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {


            BasicTextField(
                value = stringState.value,
                onValueChange = { value ->
                    if (value.length in 0..maxLength) {
                        stringState.value = value
                        if (validateNickname) {
                            if (value.isNotEmpty()) {
                                job?.cancel()
                                job = lifecycleScope.launch {
                                    delay(1500L)
                                    viewModel.checkNicknameValidation(value)
                                }
                            } else {
                                viewModel.checkNicknameValidation(null)
                            }
                        }
                    }
                },
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.gray04))
                    .padding(top = 11.dp, start = 18.dp, end = 17.dp, bottom = 9.dp),
                decorationBox = { innerTextField ->
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {

                        val hint = createRef()

                        innerTextField()
                        if (stringState.value.isEmpty()) {
                            Text(
                                modifier = Modifier.constrainAs(hint) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                },
                                text = stringResource(id = textString),
                                color = colorResource(
                                    id = R.color.gray03
                                ),
                                fontSize = 16.sp,
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                },
                cursorBrush = SolidColor(colorResource(id = R.color.main_orange)),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }


//
//    fun showStopSignupDialog() {
//        stopSignupDialog = StopSignupDialog(requireContext(), this)
//        stopSignupDialog.show()
//    }
//
//    override fun onStopSignupClick() {
//        stopSignupDialog.dismiss()
//        activity?.finish()
//    }
}