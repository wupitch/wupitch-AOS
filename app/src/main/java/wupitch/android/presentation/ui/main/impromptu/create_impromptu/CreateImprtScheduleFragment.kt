package wupitch.android.presentation.ui.main.impromptu.create_impromptu

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.create_crew.ScheduleState
import wupitch.android.util.dateFormatter

@AndroidEntryPoint
class CreateImprtScheduleFragment : Fragment() {

    private val viewModel: CreateImprtViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val scrollState = rememberScrollState(0)

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if (stopSignupState.value) {
                        val bundle = Bundle().apply { putInt("tabId", R.id.impromptuFragment) }
                        findNavController().navigate(R.id.action_createImprtScheduleFragment_to_mainFragment, bundle)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_impromptu_warning)
                        )
                    }

                    val timeState = remember { viewModel.timeState }
                    timeState.value.apply {
                        startTime = remember { startTime }
                        endTime = remember { endTime }
                        isStartTimeSet = remember { isStartTimeSet }
                        isEndTimeSet = remember { isEndTimeSet }
                    }

                    val snackbarHostState = remember { SnackbarHostState() }
                    val dateState = remember { viewModel.dateState }
                    val dateValidState = remember { viewModel.dateValidState}

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
                            textString = R.string.create_impromptu
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
                                text = stringResource(id = R.string.impromptu_schedule),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                                lineHeight = 28.sp
                            )

                            ScheduleLayout(
                                timeState, snackbarHostState, dateState, dateValidState
                            )

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
                            btnColor = if (checkValid(timeState, dateState)) R.color.main_orange else R.color.gray03,
                            textString = R.string.two_over_five,
                            fontSize = 16.sp
                        ) {
                            if (checkValid(timeState, dateState)) {
                                findNavController().navigate(R.id.action_createImprtScheduleFragment_to_createImprtImageFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDatePicker(
        activity: AppCompatActivity,
        updatedDate: (Long?) -> Unit
    ) {
        val picker =
            MaterialDatePicker.Builder.datePicker().setTitleText(R.string.impromptu_schedule_dialog)
                .setTheme(R.style.DatePickerDialogTheme).build()
        picker.show(activity.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            updatedDate(it)
        }

    }

    private fun checkValid(
        timeState: State<TimeState>,
        dateState : State<String>
    ): Boolean {

        return (timeState.value.isStartTimeSet.value == true && timeState.value.isEndTimeSet.value == true
                && dateState.value != "0000.00.00 (요일)")

    }


    @Composable
    private fun ScheduleLayout(
        timeState: State<TimeState>,
        snackbarHostState: SnackbarHostState,
        dateState: State<String>,
        dateValidState: State<Boolean?>
    ) {

        if (timeState.value.isStartTimeSet.value == null || timeState.value.isEndTimeSet.value == null) {

            LaunchedEffect(key1 = snackbarHostState, block = {
                snackbarHostState.showSnackbar(
                    message = getString(R.string.time_warning),
                    duration = SnackbarDuration.Short
                )
                if (timeState.value.isEndTimeSet.value == null) timeState.value.isEndTimeSet.value =
                    false
                if (timeState.value.isStartTimeSet.value == null) timeState.value.isStartTimeSet.value =
                    false
            })

        }

        Spacer(modifier = Modifier.height(32.dp))
        DatePickerView(dateState, dateValidState, snackbarHostState)


        Spacer(modifier = Modifier.height(32.dp))
        TimeFilter(
            timeState.value.startTime,
            timeState.value.endTime,
            timeState.value.isStartTimeSet,
            timeState.value.isEndTimeSet
        ) {
            val timeBottomSheet = TimeBottomSheetFragment(timeType = it, viewModel = viewModel)
            timeBottomSheet.show(childFragmentManager, "time bottom sheet fragment")
        }

    }

    @Composable
    private fun DatePickerView(
        dateState: State<String>,
        dateValidState : State<Boolean?>,
        snackbarHostState: SnackbarHostState,
    ) {
        Button(
            modifier = Modifier
                .width(165.dp)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (dateState.value != "0000.00.00 (요일)") colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            ),
            colors = ButtonDefaults.buttonColors(Color.White),
            elevation = null,
            onClick = {
                showDatePicker(requireActivity() as AppCompatActivity) {
                    viewModel.setDateState(it)
                }
            }
        ) {
            Text(
                text = dateState.value,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = if (dateState.value != "0000.00.00 (요일)") colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            )
        }

        if(dateValidState.value == false) {
            LaunchedEffect(key1 = snackbarHostState, block = {
                snackbarHostState.showSnackbar(
                    message = getString(R.string.date_not_valid),
                    duration = SnackbarDuration.Short
                )
                viewModel.resetDateValid()
            })
        }
    }
}