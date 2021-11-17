package wupitch.android.presentation.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val emailTextState = remember { viewModel.userEmail }
                    val pwTextState = remember { viewModel.userPw }

                    if(viewModel.loginState.value.isSuccess){
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }

                    if(viewModel.loginState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), viewModel.loginState.value.error, Toast.LENGTH_SHORT).show()
                    }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                    ) {
                        val (logo, signupText, signupBtn, inputCol, progressbar) = createRefs()


                        Image(
                            modifier = Modifier
                                .constrainAs(logo) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top, margin = 104.dp)
                                }
                                .width(159.dp)
                                .height(138.dp),
                            painter = painterResource(id = R.drawable.login_logo),
                            contentDescription = null
                        )

                        Column(Modifier.constrainAs(inputCol) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(logo.bottom, margin = 36.dp)
                            width = Dimension.fillToConstraints
                        }) {
                            LoginTextField(
                                isEmail = true,
                                hintString = stringResource(id = R.string.email),
                                textState = emailTextState
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LoginTextField(
                                hintString = stringResource(id = R.string.login_pw),
                                textState = pwTextState
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            LoginButton(
                                isLoginBtn = true,
                                onClick = {
                                if (emailTextState.value.isNotEmpty() && pwTextState.value.isNotEmpty()) {
                                    viewModel.tryLogin()
                                }
                            })
                        }

                        Text(
                            modifier = Modifier.constrainAs(signupText) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(signupBtn.top, margin = 8.dp)
                            },
                            text = stringResource(id = R.string.not_member_yet),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.gray03)
                        )
                        LoginButton(
                            modifier = Modifier.constrainAs(signupBtn){
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, margin = 32.dp)
                            },
                            onClick = {
                                findNavController().navigate(R.id.action_loginFragment_to_serviceAgreementFragment)
                        })

                        if(viewModel.loginState.value.isLoading){
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LoginButton(
        modifier: Modifier = Modifier,
        isLoginBtn: Boolean = false,
        onClick: () -> Unit
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(if (isLoginBtn) colorResource(id = R.color.main_orange) else Color.White)
                .border(
                    width = 1.dp,
                    if (isLoginBtn) Color.Transparent else colorResource(id = R.color.main_orange),
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isLoginBtn) stringResource(id = R.string.login) else stringResource(id = R.string.signup_with_email),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = if (isLoginBtn) Color.White else colorResource(id = R.color.main_orange)
            )
        }
    }

    @Composable
    fun LoginTextField(
        isEmail: Boolean = false,
        hintString: String,
        textState: MutableState<String>
    ) {

        val customTextSelectionColors = TextSelectionColors(
            handleColor = colorResource(id = R.color.gray03),
            backgroundColor = colorResource(id = R.color.gray03)
        )

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
                },
                visualTransformation = if (isEmail) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = if (isEmail) ImeAction.Next else ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    setKeyboardDown()
                }),
                cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
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
                                    text = hintString,
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

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}