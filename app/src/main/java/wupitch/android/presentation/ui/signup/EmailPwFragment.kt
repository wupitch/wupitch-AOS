package wupitch.android.presentation.ui.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.StopWarningDialog

class EmailPwFragment : Fragment() {

    private val viewModel: SignupViewModel by navGraphViewModels(R.id.signup_nav) {defaultViewModelProviderFactory}
    private var pwJob: Job? = null
    private var emailJob: Job? = null

    @ExperimentalPagerApi
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
                    if (stopSignupState.value) {
                        findNavController().navigate(R.id.action_emailPwFragment_to_onboardingFragment)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.warning_stop_signup)
                        )
                    }
                    val emailTextState = remember { mutableStateOf(viewModel.userEmail.value?:"") }
                    val pwTextState = remember { mutableStateOf(viewModel.userPw.value?:"") }

                    val isEmailValid = viewModel.isEmailValid.observeAsState().value
                    val isPwValid = viewModel.isPwValid.observeAsState().value

                    val customTextSelectionColors = TextSelectionColors(
                        handleColor = colorResource(id = R.color.gray03),
                        backgroundColor = colorResource(id = R.color.gray03)
                    )


                    ConstraintLayout(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(bottom = 32.dp)
                    ) {
                        val (toolbar, title, inputCol, nextBtn) = createRefs()


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
                                    top.linkTo(toolbar.bottom, margin = 24.dp)
                                },
                            text = stringResource(id = R.string.input_email_pw),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 32.sp
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(inputCol) {
                                    top.linkTo(title.bottom, margin = 46.dp)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }) {
                            EmailTextField(
                                textState = emailTextState,
                                customTextSelectionColors = customTextSelectionColors
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            if (emailTextState.value.isNotEmpty() && isEmailValid != null) {
                                Text(
                                    text = if(isEmailValid == true) stringResource(id = R.string.allowed_email)
                                        else stringResource(id = R.string.not_allowed_email),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = if(isEmailValid == true) colorResource(id = R.color.green)
                                        else colorResource(id = R.color.red)
                                )
                            }else {
                                Text(
                                    text = "",
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color.Transparent
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            PasswordTextField(pwTextState, customTextSelectionColors)
                            Spacer(modifier = Modifier.height(6.dp))

                            if (pwTextState.value.isNotEmpty() && isPwValid == false) {
                                Text(
                                    text = stringResource(id = R.string.not_allowed_pw),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.red)
                                )
                            }
                        }

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
                            btnColor = if(isEmailValid == true && isPwValid == true) R.color.main_orange
                            else R.color.gray03,
                            textString = R.string.next_two_over_four,
                            fontSize = 16.sp
                        ) {

                            if(isEmailValid == true && isPwValid == true){
                                findNavController().navigate(R.id.action_emailPwFragment_to_profileFragment)
                            }

                        }

                    }
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun EmailTextField(
        textState: MutableState<String>,
        customTextSelectionColors: TextSelectionColors
    ) {

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {


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

                    emailJob?.cancel()
                    emailJob = lifecycleScope.launch {
                        delay(1200L)
                        viewModel.checkEmailValid(value)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                                    text = stringResource(id = R.string.email),
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
    }

    @ExperimentalPagerApi
    @Composable
    fun PasswordTextField(
        textState: MutableState<String>,
        customTextSelectionColors: TextSelectionColors
    ) {
        val pwViewToggleState = remember { mutableStateOf(false) }

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (textField, icon) = createRefs()

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    visualTransformation = if (pwViewToggleState.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions (onDone = {
                        setKeyboardDown()
                    }),
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

                        pwJob?.cancel()
                        pwJob = lifecycleScope.launch {
                            delay(1200L)
                            viewModel.checkPwValid(value)
                        }

                    },
                    cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorResource(id = R.color.gray04))
                                .padding(start = 16.dp, top = 11.dp, bottom = 9.dp, end = 55.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            ConstraintLayout(
                                modifier = Modifier
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
                                        text = stringResource(id = R.string.pw),
                                        color = colorResource(
                                            id = R.color.gray03
                                        ),
                                        fontSize = 16.sp,
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                )

                Image(
                    modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(textField.end, margin = 20.dp)
                            height = Dimension.fillToConstraints
                        }
                        .size(24.dp)
                        .toggleable(
                            value = pwViewToggleState.value,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = true,
                            role = Role.Checkbox,
                            onValueChange = {
                                pwViewToggleState.value = it
                            }
                        ),
                    painter = if (pwViewToggleState.value) painterResource(id = R.drawable.view)
                    else painterResource(id = R.drawable.view_hide),
                    contentDescription = "delete search text",
                )

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
