package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.ToggleBtn
import wupitch.android.presentation.ui.components.StopWarningDialog

class SportFragment
    : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()


    @ExperimentalFoundationApi
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
                        findNavController().navigate(R.id.action_sportFragment_to_onboardingFragment)
                    }
                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.warning_stop_signup))
                    }

                    val scrollState = rememberScrollState()
                    val scope = rememberCoroutineScope()

                    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {

                        ConstraintLayout(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize()
                                .padding(bottom = 32.dp)
                        ) {
                            val (toolbar, scrollCol, nextBtn) = createRefs()
                            val sportsState = viewModel.sports.observeAsState()
                            val etcTextState = remember {
                                mutableStateOf("")
                            }
                            val etcClicked = remember {
                                mutableStateOf(false)
                            }

                            sportsState.value?.data?.let { result ->

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
                                    })


                                Column(modifier = Modifier
                                    .constrainAs(scrollCol) {
                                        top.linkTo(toolbar.bottom)
                                        bottom.linkTo(nextBtn.top)
                                        height = Dimension.fillToConstraints
                                    }
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)) {


                                    Text(
                                        modifier = Modifier.padding(top = 32.dp),
                                        text = stringResource(id = R.string.select_sport),
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.main_black),
                                        fontSize = 22.sp,
                                        textAlign = TextAlign.Start
                                    )

                                    Text(
                                        modifier = Modifier.padding(top = 4.dp),
                                        text = stringResource(id = R.string.select_sport_at_least_one),
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color.gray02),
                                        fontSize = 12.sp,
                                    )



                                    FlowRow(
                                        modifier = Modifier
                                            .padding(top = 28.dp),
                                        mainAxisSpacing = 16.dp,
                                        crossAxisSpacing = 16.dp
                                    ) {
                                        result.forEach { sportItem ->
                                            ToggleBtn(
                                                toggleState = sportItem.state,
                                                modifier = Modifier
                                                    .width(152.dp)
                                                    .height(48.dp),
                                                textString = sportItem.name
                                            ) {
                                                if (sportItem.sportsId == result.size && it) {
                                                    etcClicked.value = true
                                                } else if (sportItem.sportsId == result.size && !it) {
                                                    etcClicked.value = false
                                                }
                                            }
                                        }
                                    }

                                    if (etcClicked.value) {
                                        EtcSportTextField(
                                            modifier = Modifier.padding(top = 24.dp),
                                            textState = etcTextState,
                                            scope = scope,
                                            scrollState = scrollState
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
                                    btnColor = if (result.any { it.state.value }) R.color.main_orange
                                    else R.color.gray03,
                                    textString = R.string.next_three_over_five,
                                    fontSize = 16.sp
                                ) {
                                    if (result.any { it.state.value }) {
                                        findNavController().navigate(R.id.action_sportFragment_to_ageFragment)
                                        //todo : viewmodel 로 선택된 값 보내기. ex... result.filter { it.state.value }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun EtcSportTextField(
        modifier: Modifier,
        textState: MutableState<String>,
        scope: CoroutineScope,
        scrollState: ScrollState
    ) {

        val customTextSelectionColors = TextSelectionColors(
            handleColor = colorResource(id = R.color.main_orange),
            backgroundColor = colorResource(id = R.color.orange02)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

            BasicTextField(
                modifier = modifier
                    .onFocusChanged {
                        //todo : text field 클릭하면 위로 올라가게 구현하기.
                        if (it.isFocused) {
                            Log.d("{SportFragment.EtcSportTextField}", "is focused")
                            scope.launch {
                                scrollState.scrollTo(scrollState.maxValue)
                            }
                        }
                    },
                value = textState.value,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                ),
                onValueChange = { value ->
                    if (value.length <= 20) textState.value = value
                },
                cursorBrush = SolidColor(colorResource(id = R.color.main_orange)),
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                        ) {

                            val hint = createRef()

                            innerTextField()
                            if (textState.value.isEmpty()) {
                                Text(
                                    modifier = Modifier.constrainAs(hint) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    },
                                    text = stringResource(id = R.string.input_etc_sport),
                                    color = colorResource(
                                        id = R.color.gray03
                                    ),
                                    fontSize = 16.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                                .height(1.dp)
                                .background(
                                    colorResource(
                                        id = R.color.gray03
                                    )
                                )
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp),
                            text = "${textState.value.length}/20",
                            color = colorResource(
                                id = R.color.gray03
                            ),
                            fontSize = 12.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSports()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

}