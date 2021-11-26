package wupitch.android.presentation.ui.main.my_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.TitleToolbar

@AndroidEntryPoint
class MyPagePwFragment : Fragment() {


    private val viewModel: MyPageViewModel by viewModels()
    private var currentPwJob: Job? = null
    private var newPwJob: Job? = null

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val currentPw = remember { mutableStateOf("") }
                    val newPw = remember { mutableStateOf("") }

                    val isCurrentPwValid = remember { viewModel.isCurrentPwValid }
                    val isNewPwValid = remember { viewModel.isNewPwValid }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = colorResource(
                                    id = R.color.white
                                )
                            )
                    ) {
                        val (toolbar, pwCol, nextBtn) = createRefs()

                        TitleToolbar(
                            modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            textString = R.string.setting_change_pw
                        ) { findNavController().navigateUp() }


                        Column(Modifier.constrainAs(pwCol) {
                            top.linkTo(toolbar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {

                            Spacer(modifier = Modifier.height(24.dp))

                            PasswordTextField(
                                textState = currentPw,
                                hintString = stringResource(id = R.string.current_pw),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions.Default,
                                onValueChange = { value ->
                                    currentPw.value = value
                                    if (currentPw.value.isEmpty()) {
                                        viewModel.checkCurrentPwValid("")
                                    }
                                    currentPwJob?.cancel()
                                    currentPwJob = lifecycleScope.launch {
                                        delay(1200L)
                                        viewModel.checkCurrentPwValid(value)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            if (currentPw.value.isNotEmpty() && isCurrentPwValid.value == false) {
                                Text(
                                    modifier = Modifier.padding(start = 20.dp),
                                    text = stringResource(id = R.string.incorrect_pw),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.red)
                                )
                            } else {
                                Text(
                                    text = "",
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color.Transparent
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            PasswordTextField(
                                textState = newPw,
                                hintString = stringResource(id = R.string.new_pw),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    setKeyboardDown()
                                }),
                                onValueChange = { value ->
                                    if (isCurrentPwValid.value == true) {
                                        newPw.value = value
                                        if (newPw.value.isEmpty()) {
                                            viewModel.checkNewPwValid("")
                                        }
                                        newPwJob?.cancel()
                                        newPwJob = lifecycleScope.launch {
                                            delay(1200L)
                                            viewModel.checkNewPwValid(value)
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            if (newPw.value.isNotEmpty() && isNewPwValid.value == false) {
                                Text(
                                    modifier = Modifier.padding(start = 20.dp),
                                    text = stringResource(id = R.string.not_allowed_pw),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.red)
                                )
                            } else {
                                Text(
                                    text = "",
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color.Transparent
                                )
                            }
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
                            btnColor = if(isCurrentPwValid.value == true && isNewPwValid.value == true) R.color.main_orange
                            else R.color.gray03,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            if(isCurrentPwValid.value == true && isNewPwValid.value == true){

                            //todo 잘 보내졌으면...
                                findNavController().navigateUp()
                            }
                        }
                    }

                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    private fun PasswordTextField(
        textState: MutableState<String>,
        hintString: String,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        onValueChange: (String) -> Unit
    ) {
        val pwViewToggleState = remember { mutableStateOf(false) }
        val customTextSelectionColors = TextSelectionColors(
            handleColor = colorResource(id = R.color.gray03),
            backgroundColor = colorResource(id = R.color.gray03)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (textField, icon) = createRefs()

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    visualTransformation = if (pwViewToggleState.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    value = textState.value,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    onValueChange = onValueChange,
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
                                        text = hintString,
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
                            end.linkTo(textField.end, margin = 40.dp)
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

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}