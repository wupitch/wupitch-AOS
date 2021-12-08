package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

@AndroidEntryPoint
class CreateCrewScheduleFragment : Fragment() {

    private var checkedRadioButton: MutableState<Boolean>? = null
    private var checkedRadioButton2: MutableState<Boolean>? = null
    private var checkedRadioButton3: MutableState<Boolean>? = null

    private val viewModel: CreateCrewViewModel by navGraphViewModels(R.id.create_crew_nav) { defaultViewModelProviderFactory }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val scrollState = rememberScrollState(0)
                    val scope = rememberCoroutineScope()


                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if (stopSignupState.value) {
                        findNavController().navigate(R.id.action_createCrewScheduleFragment_to_mainFragment)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_crew_warning)
                        )
                    }


                    val scheduleList = remember { viewModel.scheduleList }

                    val firstBtnToggleState = remember { mutableStateOf(scheduleList.size > 1) }
                    val secondBtnToggleState = remember { mutableStateOf(scheduleList.size > 2) }

                    val snackbarHostState = remember { SnackbarHostState() }

                    val heightToScroll = remember { mutableStateOf(0) }

                    val warningState = remember { mutableStateOf(false) }
                    if (warningState.value) {
                        LaunchedEffect(key1 = snackbarHostState, block = {
                            snackbarHostState.showSnackbar(
                                message = getString(R.string.same_day_warning),
                                duration = SnackbarDuration.Short
                            )
                            warningState.value = false
                        })
                    }

                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, topDivider, content, bottomDivider, nextBtn, snackbar) = createRefs()

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
                            .padding(horizontal = 20.dp)
                            .onGloballyPositioned { coordinates ->
                                heightToScroll.value = coordinates.size.height
                            })
                        {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = stringResource(id = R.string.crew_schedule),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                                lineHeight = 28.sp
                            )


                            scheduleLayout(
                                0, warningState, scheduleList, scheduleList[0], snackbarHostState
                            )
                            PlusButton(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                toggleState = firstBtnToggleState
                            )

                            if (firstBtnToggleState.value) {
                                if (scheduleList.size == 1) {
                                    viewModel.addCrewSchedule()
                                }
                                scheduleLayout(
                                    1,
                                    warningState,
                                    scheduleList,
                                    scheduleList[1],
                                    snackbarHostState
                                )
                                PlusButton(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    toggleState = secondBtnToggleState
                                )
                                SideEffect {
                                    scope.launch {
                                        scrollState.animateScrollTo(
                                            heightToScroll.value,
                                            FloatSpringSpec(1.5f, 40f)
                                        )
                                    }
                                }
                            } else {
                                if (scheduleList.size == 2) {
                                    scheduleList.removeAt(1)
                                } else if (scheduleList.size == 3) {
                                    scheduleList.removeRange(1, 2)
                                }
                            }


                            if (secondBtnToggleState.value) {
                                if (scheduleList.size == 2) {
                                    viewModel.addCrewSchedule()
                                }
                                scheduleLayout(
                                    2,
                                    warningState,
                                    scheduleList,
                                    scheduleList[2],
                                    snackbarHostState
                                )
                                Spacer(modifier = Modifier.height(60.dp))
                                SideEffect {
                                    scope.launch {
                                        scrollState.animateScrollTo(
                                            heightToScroll.value,
                                            FloatSpringSpec(1.5f, 40f)
                                        )
                                    }
                                }
                            } else {
                                if (scheduleList.size == 3) {
                                    scheduleList.removeAt(2)
                                }
                            }
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

                        ShowSnackbar(
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier.constrainAs(snackbar) {
                                start.linkTo(parent.start, margin = 24.dp)
                                end.linkTo(parent.end, margin = 24.dp)
                                bottom.linkTo(nextBtn.top, margin = 24.dp)
                                width = Dimension.fillToConstraints
                            })

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
                            btnColor = if (checkValid(scheduleList)) R.color.main_orange else R.color.gray03,
                            textString = R.string.four_over_seven,
                            fontSize = 16.sp
                        ) {
                            if (checkValid(scheduleList)) {
//                                scheduleList.forEach {
//                                    Log.d("{CreateCrewScheduleFragment.onCreateView}", it.toString())
//                                }
                                findNavController().navigate(R.id.action_createCrewScheduleFragment_to_createCrewImageFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkValid(
        scheduleList: SnapshotStateList<ScheduleState>
    ): Boolean {
        scheduleList.forEach {
            if (it.day.value == -1) {
                return false
            }
            if (it.isStartTimeSet.value != true || it.isEndTimeSet.value != true) {
                return false
            }
        }
        return true
    }

    @Composable
    private fun PlusButton(
        modifier: Modifier,
        toggleState: MutableState<Boolean>
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Box(modifier = modifier
            .clip(CircleShape)
            .background(colorResource(id = R.color.gray03))
            .size(44.dp)
            .toggleable(
                value = toggleState.value,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = true,
                role = Role.Checkbox,
                onValueChange = {
                    toggleState.value = it
                }
            ), contentAlignment = Alignment.Center) {
            Icon(
                painter = if (toggleState.value) painterResource(id = R.drawable.ic_subtract)
                else painterResource(id = R.drawable.ic_btn_06_add),
                contentDescription = "plus icon",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(48.dp))

    }

    @Composable
    private fun scheduleLayout(
        index: Int,
        warningState: MutableState<Boolean>,
        scheduleList: SnapshotStateList<ScheduleState>,
        scheduleState: ScheduleState,
        snackbarHostState: SnackbarHostState
    ): ScheduleState {

        val dayList = arrayListOf<FilterItem>(
            FilterItem(stringResource(id = R.string.monday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.tuesday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.wednesday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.thursday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.friday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.saturday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.sunday), remember { mutableStateOf(false) })
        )

        scheduleState.apply {
            day = remember { day }
            startTime = remember { startTime }
            endTime = remember { endTime }
            isStartTimeSet = remember { isStartTimeSet }
            isEndTimeSet = remember { isEndTimeSet }
        }

        if (scheduleState.day.value != -1) {
            dayList[scheduleState.day.value].state.value = true
        }



        if (scheduleState.isStartTimeSet.value == null || scheduleState.isEndTimeSet.value == null) {

            LaunchedEffect(key1 = snackbarHostState, block = {
                snackbarHostState.showSnackbar(
                    message = getString(R.string.time_warning),
                    duration = SnackbarDuration.Short
                )
                if (scheduleState.isEndTimeSet.value == null) scheduleState.isEndTimeSet.value =
                    false
                if (scheduleState.isStartTimeSet.value == null) scheduleState.isStartTimeSet.value =
                    false
            })

        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.day),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        NonRepetitionLayout(
            itemList = dayList,
            listIndex = index,
            warningState = warningState,
            scheduleList = scheduleList
        ) {
            scheduleState.day.value = it
        }

        Spacer(modifier = Modifier.height(32.dp))
        TimeFilter(
            scheduleState.startTime,
            scheduleState.endTime,
            scheduleState.isStartTimeSet,
            scheduleState.isEndTimeSet
        ) {
            val timeBottomSheet = TimeBottomSheetFragment(index, it, viewModel)
            timeBottomSheet.show(childFragmentManager, "time bottom sheet fragment")
        }


        return scheduleState
    }

    @Composable
    fun NonRepetitionLayout(
        itemList: List<FilterItem>,
        listIndex: Int,
        warningState: MutableState<Boolean>,
        scheduleList: SnapshotStateList<ScheduleState>,
        onClick: (index: Int) -> Unit
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            if (listIndex == 0) {
                itemList.forEachIndexed { index, item ->
                    RadioButton(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp),
                        checkedState = item.state,
                        text = item.name,
                        index = index,
                        warningState = warningState,
                        scheduleList = scheduleList
                    ) {
                        onClick(index)
                    }
                }

            } else if (listIndex == 1) {
                itemList.forEachIndexed { index, item ->
                    RadioButton2(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp),
                        checkedState = item.state,
                        text = item.name,
                        index = index,
                        warningState = warningState,
                        scheduleList = scheduleList
                    ) {
                        onClick(index)
                    }
                }
            } else {
                itemList.forEachIndexed { index, item ->
                    RadioButton3(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp),
                        checkedState = item.state,
                        text = item.name,
                        index = index,
                        warningState = warningState,
                        scheduleList = scheduleList
                    ) {
                        onClick(index)
                    }
                }
            }
        }
    }

    @Composable
    fun RadioButton(
        modifier: Modifier,
        checkedState: MutableState<Boolean>,
        text: String,
        index: Int,
        warningState: MutableState<Boolean>,
        scheduleList: SnapshotStateList<ScheduleState>,
        onClick: () -> Unit
    ) {
        if (checkedState.value) {
            checkedRadioButton = checkedState
        }
        Box(
            modifier = modifier
                .selectable(
                    selected = checkedState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.RadioButton,
                    onClick = {

                        when (scheduleList.size) {
                            1 -> {
                                checkedRadioButton?.value = false
                                checkedState.value = true
                                checkedRadioButton = checkedState
                                onClick()
                            }
                            2 -> {
                                if (index == scheduleList[1].day.value) {
                                    warningState.value = true
                                    return@selectable
                                } else {
                                    checkedRadioButton?.value = false
                                    checkedState.value = true
                                    checkedRadioButton = checkedState
                                    onClick()
                                }
                            }
                            3 -> {
                                if (index == scheduleList[1].day.value || index == scheduleList[2].day.value) {
                                    warningState.value = true
                                    return@selectable
                                } else {
                                    checkedRadioButton?.value = false
                                    checkedState.value = true
                                    checkedRadioButton = checkedState
                                    onClick()
                                }
                            }
                        }

                    }
                )
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (checkedState.value) colorResource(id = R.color.orange02) else
                        colorResource(id = R.color.gray01)
                )
                .border(
                    color = if (checkedState.value) colorResource(id = R.color.main_orange)
                    else Color.Transparent,
                    width = if (checkedState.value) 1.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = if (checkedState.value) colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            )
        }
    }

    @Composable
    fun RadioButton2(
        modifier: Modifier,
        checkedState: MutableState<Boolean>,
        text: String,
        index: Int,
        warningState: MutableState<Boolean>,
        scheduleList: SnapshotStateList<ScheduleState>,
        onClick: () -> Unit
    ) {
        if (checkedState.value) {
            checkedRadioButton2 = checkedState
        }
        Box(
            modifier = modifier
                .selectable(
                    selected = checkedState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.RadioButton,
                    onClick = {

                        when (scheduleList.size) {
                            2 -> {
                                if (index == scheduleList[0].day.value) {
                                    warningState.value = true
                                    return@selectable
                                } else {
                                    checkedRadioButton2?.value = false
                                    checkedState.value = true
                                    checkedRadioButton2 = checkedState
                                    onClick()
                                }
                            }
                            3 -> {
                                if (index == scheduleList[0].day.value || index == scheduleList[2].day.value) {
                                    warningState.value = true
                                    return@selectable
                                } else {
                                    checkedRadioButton2?.value = false
                                    checkedState.value = true
                                    checkedRadioButton2 = checkedState
                                    onClick()
                                }
                            }
                        }

                    }
                )
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (checkedState.value) colorResource(id = R.color.orange02) else
                        colorResource(id = R.color.gray01)
                )
                .border(
                    color = if (checkedState.value) colorResource(id = R.color.main_orange)
                    else Color.Transparent,
                    width = if (checkedState.value) 1.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = if (checkedState.value) colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            )
        }
    }

    @Composable
    fun RadioButton3(
        modifier: Modifier,
        checkedState: MutableState<Boolean>,
        text: String,
        index: Int,
        warningState: MutableState<Boolean>,
        scheduleList: SnapshotStateList<ScheduleState>,
        onClick: () -> Unit
    ) {
        if (checkedState.value) {
            checkedRadioButton3 = checkedState
        }
        Box(
            modifier = modifier
                .selectable(
                    selected = checkedState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.RadioButton,
                    onClick = {

                        if (index == scheduleList[0].day.value || index == scheduleList[1].day.value) {
                            warningState.value = true
                            return@selectable
                        } else {
                            checkedRadioButton3?.value = false
                            checkedState.value = true
                            checkedRadioButton3 = checkedState
                            onClick()
                        }
                    }
                )
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (checkedState.value) colorResource(id = R.color.orange02) else
                        colorResource(id = R.color.gray01)
                )
                .border(
                    color = if (checkedState.value) colorResource(id = R.color.main_orange)
                    else Color.Transparent,
                    width = if (checkedState.value) 1.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = if (checkedState.value) colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            )
        }
    }
}