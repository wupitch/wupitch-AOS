package wupitch.android.presentation.ui.main.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.TitleToolbar
import wupitch.android.presentation.ui.components.ToggleBtn

class FilterFragment : Fragment() {

    private val sportsList = listOf<FilterItem>(
        FilterItem(R.string.soccer_football),
        FilterItem(R.string.badminton),
        FilterItem(R.string.volleyball),
        FilterItem(R.string.basketball),
        FilterItem(R.string.hiking),
        FilterItem(R.string.running)
    )

    private val dateList = listOf<FilterItem>(
        FilterItem(R.string.monday), FilterItem(R.string.tuesday),
        FilterItem(R.string.wednesday), FilterItem(R.string.thursday), FilterItem(R.string.friday),
        FilterItem(R.string.saturday), FilterItem(R.string.sunday)
    )
    private val ageGroupList = listOf<FilterItem>(
        FilterItem(R.string.teenager),
        FilterItem(R.string.twenties),
        FilterItem(R.string.thirties),
        FilterItem(R.string.forties),
        FilterItem(R.string.over_fifties),
        FilterItem(R.string.regardless_of_age)
    )
    private val crewSizeList = listOf<FilterItem>(
        FilterItem(R.string.less_than_ten),
        FilterItem(R.string.more_than_ten_less_than_thirty),
        FilterItem(R.string.more_than_thirty_less_than_fifty),
        FilterItem(R.string.more_than_fifty_less_than_seventy),
        FilterItem(R.string.more_than_seventy)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    ConstraintLayout(Modifier.fillMaxSize()) {
                        val scrollState = rememberScrollState(0)

                        val (toolbar, topDivider, filterContents, bottomDivider, buttons) = createRefs()
                        TitleToolbar(
                            modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            textString = R.string.filter
                        ) {
                            findNavController().navigateUp()
                        }
                        Divider(
                            Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(filterContents) {
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(buttons.top)
                                    height = Dimension.fillToConstraints
                                }
                                .padding(horizontal = 20.dp)
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            RepetitionFilter(
                                text = R.string.event,
                                filterItemList = sportsList,
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp)
                            ) {
                                Log.d("{FilterFragment.onCreateView}", "종목 : $it")
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            RepetitionFilter(
                                text = R.string.day,
                                filterItemList = dateList,
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                            ) {
                                Log.d("{FilterFragment.onCreateView}", "요일 : $it")
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            TimeFilter()

                            Spacer(modifier = Modifier.height(32.dp))
                            NonRepetitionFilter(
                                text = R.string.crew_member_num,
                                filterItemList = crewSizeList,
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp)
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            RepetitionFilter(
                                text = R.string.age_group,
                                filterItemList = ageGroupList,
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp)
                            ) {
                                Log.d("{FilterFragment.onCreateView}", "연령 : $it")
                            }

                            Spacer(modifier = Modifier.height(60.dp))



                        }
                        Divider(
                            Modifier
                                .constrainAs(bottomDivider) {
                                    bottom.linkTo(buttons.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )
                        FilterBottomButtons(
                            modifier = Modifier.constrainAs(buttons) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )


                    }


                }
            }
        }
    }

    @Composable
    fun TimeFilter() {
        val startTimeState = remember {
            mutableStateOf("12:00")
        }
        val endTimeState = remember {
            mutableStateOf("12:00")
        }
        Column(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = R.string.time),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeButton(startTimeState)
                Spacer(modifier = Modifier.width(4.dp))
                Divider(
                    Modifier
                        .width(8.dp)
                        .height(1.dp)
                        .background(if(endTimeState.value == "00:00") colorResource(id = R.color.gray02)
                        else colorResource(id = R.color.main_orange))
                )
                Spacer(modifier = Modifier.width(4.dp))
                TimeButton(endTimeState)
            }

        }

    }


    @Composable
    private fun TimeButton(
        timeState: MutableState<String>
    ) {
        Button(
            modifier = Modifier
                .width(96.dp)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 1.dp, color = if (timeState.value == "00:00") colorResource(id = R.color.gray02)
            else colorResource(id = R.color.main_orange)),
            colors = ButtonDefaults.buttonColors(Color.White),
            elevation = null,
            onClick = {}
        ) {
            Text(
                text = timeState.value,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = if (timeState.value != "00:00") colorResource(id = R.color.main_orange)
                else colorResource(id = R.color.gray02)
            )
        }
    }

    @Composable
    private fun DatePicker() {

    }

    @Composable
    fun NonRepetitionFilter(
        text: Int,
        filterItemList: List<FilterItem>,
        modifier: Modifier
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = text),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            FlowRow(
                modifier = Modifier.padding(top = 12.dp),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 16.dp
            ) {
                filterItemList.forEachIndexed { index, item ->
                    RadioButton(
                        modifier = modifier,
                        checkedState = item.state,
                        text = item.name,
                        index = index
                    ) {
                        Log.d("{FilterFragment.NonRepetitionFilter}", "크루원 수 : $it")
                    }
                }
            }
        }
    }

    private var checkedRadioButton: MutableState<Boolean>? = null

    @Composable
    fun RadioButton(
        modifier: Modifier,
        checkedState: MutableState<Boolean>,
        text: Int,
        index: Int,
        onClick: (index: Int) -> Unit
    ) {
        Box(
            modifier = modifier
                .selectable(
                    selected = checkedState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.RadioButton,
                    onClick = {
                        checkedRadioButton?.value = false
                        checkedState.value = true
                        checkedRadioButton = checkedState
                        onClick(index)
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
                text = stringResource(id = text),
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
    private fun RepetitionFilter(
        text: Int,
        filterItemList: List<FilterItem>,
        modifier: Modifier,
        onClick: (index: Int) -> Unit
    ) {

        Column(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = text),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            FlowRow(
                modifier = Modifier
                    .padding(top = 12.dp),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 16.dp
            ) {
                filterItemList.forEachIndexed { index, item ->
                    ToggleBtn(
                        toggleState = item.state,
                        modifier = modifier,
                        textString = stringResource(id = item.name)
                    ) {
                        if (it) {
                            onClick(index)
                        }
                    }
                }
            }

        }

    }


    @Composable
    fun FilterBottomButtons(
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
                .height(68.dp)
                .padding(vertical = 12.dp)
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            val (init, apply) = createRefs()

            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .background(Color.White)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            //todo 초기화.
                        }
                    ), contentAlignment = Alignment.Center
//                    .constrainAs(init) {
//                        start.linkTo(parent.start)
//                        end.linkTo(apply.start)
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                    }


            ) {
                Text(
                    text = stringResource(id = R.string.initialize),
                    fontSize = 14.sp, fontFamily = Roboto, fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.gray02)
                )
            }

            RoundBtn(
                modifier = Modifier
                    .weight(0.65f)
//                    .constrainAs(apply) {
//                        end.linkTo(parent.end)
////                        top.linkTo(parent.top)
////                        bottom.linkTo(parent.bottom)
////                        start.linkTo(init.end)
//                        width = Dimension.fillToConstraints
//                    }
                    .width(208.dp)
                    .height(44.dp),
                btnColor = R.color.main_orange,
                textString = R.string.apply, fontSize = 16.sp
            ) {
                //todo apply button function.
            }


        }
    }
}