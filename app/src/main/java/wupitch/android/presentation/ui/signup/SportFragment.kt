package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.LastBaseline
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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.data.remote.dto.toSportResult
import wupitch.android.databinding.FragmentSportBinding
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.SetToolBar
import wupitch.android.presentation.ui.signup.components.SportToggleBtn

class SportFragment
    : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var stopSignupDialog: StopSignupDialog


    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

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

                                SetToolBar(modifier = Modifier.constrainAs(toolbar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }, onLeftIconClick = {
                                    findNavController().navigateUp()
                                }, textString = null,
                                hasRightIcon = true,
                                onRightIconClick = {
                                    //todo stop dialog.
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
                                        modifier = Modifier.padding(top = 28.dp),
                                        mainAxisSpacing = 16.dp,
                                        crossAxisSpacing = 16.dp
                                    ) {
                                        result.forEach { sportItem ->
                                            SportToggleBtn(
                                                toggleState = sportItem.state,
                                                modifier = Modifier
                                                    .width(152.dp)
                                                    .height(48.dp),
                                                textString = sportItem.name
                                            ) {
                                                if(sportItem.sportsId == result.size && it){
                                                    etcClicked.value = true
                                                } else if (sportItem.sportsId == result.size && !it){
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
                                    btnColor = if(result.any { it.state.value }) R.color.main_orange
                                    else R.color.gray03,
                                    textString = R.string.next_three_over_five,
                                    fontSize = 16.sp
                                ) {
                                    if(result.any { it.state.value }) {
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

        BasicTextField(modifier = modifier
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
            onValueChange = {
                textState.value = it
            },
            textStyle = TextStyle(color = Color.Black),
            decorationBox = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {

//                        if (LocalWindowInsets.current.ime.isVisible) {
//                            BringIntoViewRequester()
//                        }
                        if (textState.value.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.input_etc_sport),
                                color = colorResource(
                                    id = R.color.gray03
                                ),
                                fontSize = 16.sp,
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal
                            )
                        } else {
//                            BringIntoViewRequester()
                            Text(
                                text = textState.value,
                                color = colorResource(
                                    id = R.color.main_black
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSports()
    }


//    private fun openTalentBottomSheet(viewTag : Int) {
//        //실력선택하고 나서 선택했다는 표시 같은거 버튼에서 볼 수 있으면 좋겠는데...
//        talentBottomSheet = SportTalentBottomSheetFragment(viewModel, viewTag)
//        talentBottomSheet.show(childFragmentManager, "sport_talent_bottom_sheet")
//    }

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