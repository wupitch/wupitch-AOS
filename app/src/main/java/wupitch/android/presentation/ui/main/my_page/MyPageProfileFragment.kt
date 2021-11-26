package wupitch.android.presentation.ui.main.my_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.RoundBtn

@AndroidEntryPoint
class MyPageProfileFragment : Fragment() {

    private val viewModel: MyPageViewModel by viewModels()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    ConstraintLayout(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(bottom = 32.dp)
                    ) {
                        val (toolbar, title, nicknameEt, introEt, nicknameValidation, introCounter, nextBtn) = createRefs()

                        //todo 처음에 유저 정보 가져오기.
                        val nicknameState = remember { mutableStateOf(viewModel.userNickname.value ?: "") }
                        val introState = remember { mutableStateOf(viewModel.userIntroduce.value?: "") }

                        val isNicknameValidState = viewModel.isNicknameValid.observeAsState()


                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            hasRightIcon = true,
                            onRightIconClick = { findNavController().navigateUp() }
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(title) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(toolbar.bottom, margin = 24.dp)
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
                            btnColor = if (introState.value.isNotEmpty() && isNicknameValidState.value == true && nicknameState.value.isNotEmpty()) R.color.main_orange
                            else R.color.gray03,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            if (introState.value.isNotEmpty() &&
                                isNicknameValidState.value == true &&
                                nicknameState.value.isNotEmpty()
                            ) {
                                viewModel.setUserIntroduce(introState.value)
                                //todo patch profile 되면 navigate up.
                                findNavController().navigateUp()

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
            handleColor = colorResource(id = R.color.gray03),
            backgroundColor = colorResource(id = R.color.gray03)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {


            BasicTextField(
                value = stringState.value,
                onValueChange = { value ->
                    if (value.length in 0..maxLength) {
                        stringState.value = value
                        if (validateNickname) {

                            if(stringState.value.isEmpty()){
                                viewModel.checkNicknameValid("")
                            }else {
                                job?.cancel()
                                job = lifecycleScope.launch {
                                    delay(1200L)
                                    viewModel.checkNicknameValid(value)
                                }
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = if(validateNickname) ImeAction.Next else ImeAction.Done),
                keyboardActions = KeyboardActions (onDone = {
                    setKeyboardDown()
                }),
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
                cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}