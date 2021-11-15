package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.SimpleTextField
import wupitch.android.presentation.ui.components.StopWarningDialog

class EmailPwFragment : Fragment() {

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
                        findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.warning_stop_signup)
                        )
                    }
                    val emailTextState = remember { mutableStateOf("") }
                    val pwTextState = remember { mutableStateOf("") }


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
                                    top.linkTo(toolbar.bottom, margin = 32.dp)
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
                            SimpleTextField(
                                textState = emailTextState,
                                hintText = stringResource(id = R.string.email)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(id = R.string.allowed_email),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.green)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            PasswordTextField(pwTextState)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(id = R.string.not_allowed_pw),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.red)
                            )

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
                            btnColor = R.color.gray03,
                            textString = R.string.next_two_over_four,
                            fontSize = 16.sp
                        ) {

                            findNavController().navigate(R.id.action_emailPwFragment_to_profileFragment)
                            //todo : viewmodel 에 이메일, 비밀번호 보내기

                        }

                    }
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun PasswordTextField(
        textState: MutableState<String>
    ) {
        val pwViewToggleState = remember { mutableStateOf(false) }

        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (textField, icon) = createRefs()

            BasicTextField(
                modifier = Modifier.fillMaxWidth().constrainAs(textField){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                visualTransformation = if (pwViewToggleState.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                painter = if(pwViewToggleState.value) painterResource(id = R.drawable.view)
                else  painterResource(id = R.drawable.view_hide),
                contentDescription = "delete search text",
            )

        }
    }
}
