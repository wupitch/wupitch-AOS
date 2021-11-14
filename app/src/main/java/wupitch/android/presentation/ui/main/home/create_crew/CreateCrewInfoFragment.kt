package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.create_crew.components.CreateCrewRepetitionLayout
import wupitch.android.presentation.ui.components.NumberTextFieldLayout

@ExperimentalPagerApi
class CreateCrewInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val scrollState = rememberScrollState(0)

                    val crewNameState = remember { mutableStateOf("") }
                    val crewSizeState = remember { mutableStateOf("") }

                    val crewAgeGroupListState = remember { mutableStateListOf<Int>() }
                    val crewExtraInfoListState = remember { mutableStateListOf<Int>() }

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if(stopSignupState.value) {
                        findNavController().navigate(R.id.action_createCrewInfoFragment_to_mainFragment)
                    }
                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_crew_warning))
                    }

                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, topDivider, content, bottomDivider, nextBtn) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { dialogOpenState.value = true },
                            textString = R.string.create_crew
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        Column(modifier = Modifier
                            .constrainAs(content) {
                                top.linkTo(topDivider.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomDivider.top)
                                height = Dimension.fillToConstraints
                                width = Dimension.fillToConstraints
                            }
                            .verticalScroll(scrollState)
                            .padding(horizontal = 20.dp))
                        {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = stringResource(id = R.string.crew_basic_info),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            CrewName(crewNameState)

                            Spacer(modifier = Modifier.height(32.dp))
                            CurrentCrewSize(crewSizeState)

                            Spacer(modifier = Modifier.height(32.dp))
                            CrewAgeGroup(crewAgeGroupListState)

                            Spacer(modifier = Modifier.height(32.dp))
                            CrewExtraInfo(crewExtraInfoListState)

                            Spacer(modifier = Modifier.height(60.dp))
                        }

                        Divider(
                            modifier = Modifier
                                .constrainAs(bottomDivider) {
                                    bottom.linkTo(nextBtn.top, margin = 12.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom, margin = 24.dp)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            //todo update 가 안 됌.
                            btnColor = if (crewAgeGroupListState.isNotEmpty() && crewNameState.value != "" && crewSizeState.value != "") R.color.main_orange else R.color.gray03,
                            textString = R.string.three_over_seven,
                            fontSize = 16.sp
                        ) {
                            if (crewNameState.value != "" && crewSizeState.value != ""
                                && crewAgeGroupListState.isNotEmpty()
                            ) {

                                findNavController().navigate(R.id.action_createCrewInfoFragment_to_createCrewScheduleFragment)
                                //todo extra info state value 와 함께 viewModel 로!!!
//                                crewExtraInfoListState.forEach {
//                                    Log.d("{CreateCrewInfoFragment.onCreateView}", it.toString())
//                                }

                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CrewExtraInfo(
        crewExtraInfoListState: SnapshotStateList<Int>
    ) {

        val ageGroupList = listOf<FilterItem>(
            FilterItem(getString(R.string.beginner_centered), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.semi_expert_centered), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.expert_centered), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.beginners_to_experts), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.train_with_coach), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.has_lesson), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.train_centered), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.game_centered), remember{ mutableStateOf(false)})
        )

        Text(
            text = stringResource(id = R.string.extra_info),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.repetition_possible),
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.gray02),
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        CreateCrewRepetitionLayout(
            filterItemList = ageGroupList,
            extraInfoListState = crewExtraInfoListState
        )

    }

    @Composable
    private fun CrewAgeGroup(
        crewAgeGroupState: SnapshotStateList<Int>
    ) {

        val ageGroupList = listOf<FilterItem>(
            FilterItem(getString(R.string.teenager), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.twenties), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.thirties), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.forties), remember{ mutableStateOf(false)}),
            FilterItem(getString(R.string.over_fifties), remember{ mutableStateOf(false)})
        )

        Text(
            text = stringResource(id = R.string.age_group),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.repetition_possible),
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.gray02),
            fontSize = 12.sp,
        )
        RepetitionLayout(
            text = null,
            filterItemList = ageGroupList,
            modifier = Modifier
                .width(96.dp)
                .height(48.dp),
            checkedListState = crewAgeGroupState
        )


    }

    @Composable
    private fun CurrentCrewSize(
        textState: MutableState<String>
    ) {
        Text(
            text = stringResource(id = R.string.current_crew_size),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))

        NumberTextFieldLayout(
            modifier = Modifier
                .width(59.dp)
                .height(44.dp),
            textState = textState,
            measureString = stringResource(id = R.string.people_count_measure)
        )

    }

    @Composable
    private fun CrewName(
        textState: MutableState<String>
    ) {
        Text(
            text = stringResource(id = R.string.crew_name),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        SimpleTextField(
            textState = textState,
            hintText = stringResource(id = R.string.input_crew_name)
        )
    }

}