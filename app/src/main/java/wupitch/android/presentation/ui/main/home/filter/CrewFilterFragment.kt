package wupitch.android.presentation.ui.main.home.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.HomeViewModel

@AndroidEntryPoint
class CrewFilterFragment : Fragment() {

    private var checkedRadioButton: MutableState<Boolean>? = null
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCrewFilter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val sportsList = listOf<FilterItem>(
                        FilterItem(
                            getString(R.string.soccer_football),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.badminton),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.volleyball),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.basketball),
                            remember { mutableStateOf(false) }),
                        FilterItem(getString(R.string.hiking), remember { mutableStateOf(false) }),
                        FilterItem(getString(R.string.running), remember { mutableStateOf(false) })
                    )

                    val dayList = listOf<FilterItem>(
                        FilterItem(
                            stringResource(id = R.string.monday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.tuesday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.wednesday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.thursday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.friday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.saturday),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            stringResource(id = R.string.sunday),
                            remember { mutableStateOf(false) })
                    )
                    val ageGroupList = listOf<FilterItem>(
                        FilterItem(
                            getString(R.string.teenager),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.twenties),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.thirties),
                            remember { mutableStateOf(false) }),
                        FilterItem(getString(R.string.forties), remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.over_fifties),
                            remember { mutableStateOf(false) })
                    )
                    val crewSizeList = listOf<FilterItem>(
                        FilterItem(
                            getString(R.string.less_than_ten),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.more_than_ten_less_than_thirty),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.more_than_thirty_less_than_fifty),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.more_than_fifty_less_than_seventy),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.more_than_seventy),
                            remember { mutableStateOf(false) })
                    )

                    val resetState = remember { viewModel.resetState }
                    if (resetState.value) {
                        sportsList.forEach { it.state.value = false }
                        dayList.forEach { it.state.value = false }
                        ageGroupList.forEach { it.state.value = false }
                    }

                    val filterState = remember { viewModel.getCrewFilterState }
                    if (filterState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            filterState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val eventState = remember { viewModel.crewEventList }
                    if (eventState.size > 0) {
                        eventState.forEach { item ->
                            sportsList[item].state.value = true
                        }
                    }
                    val dayState = remember { viewModel.crewDayList }
                    if (dayState.size > 0) {
                        dayState.forEach { item ->
                            dayList[item].state.value = true
                        }
                    }
                    val ageGroupState = remember { viewModel.crewAgeGroupList }
                    if (ageGroupState.size > 0) {
                        ageGroupState.forEach { item ->
                            ageGroupList[item].state.value = true
                        }
                    }


                    val crewSizeState = remember { viewModel.crewSizeState }
                    if (crewSizeState.value != null) {
                        crewSizeList[crewSizeState.value!!].state.value = true
                    }else {
                        crewSizeList.forEach { if(it.state.value) it.state.value = false }
                    }

                    if(viewModel.filterApplied.value){
                        findNavController().navigateUp()
                        viewModel.filterApplied.value = false
                    }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val scrollState = rememberScrollState(0)


                        val (toolbar, topDivider, filterContents, bottomDivider, buttons, snackbar) = createRefs()
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
                                .verticalScroll(scrollState)
                                .padding(horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            RepetitionLayout(
                                text = R.string.event,
                                filterItemList = sportsList,
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp),
                                checkedListState = eventState
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            RepetitionLayout(
                                text = R.string.day,
                                filterItemList = dayList,
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                checkedListState = dayState
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            NonRepetitionLayout(
                                itemList = crewSizeList
                            ) {
                                viewModel.setCrewSize(it)
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            RepetitionLayout(
                                text = R.string.age_group,
                                filterItemList = ageGroupList,
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp),
                                checkedListState = ageGroupState
                            )

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
                            },
                            onApplyClick = {
                                viewModel.setCrewSize(crewSizeState.value)
                                viewModel.setCrewAgeGroupList(ageGroupState)
                                viewModel.setCrewDayList(dayState)
                                viewModel.setCrewEventList(eventState)
                                viewModel.applyFilter()
                            },
                            onResetClick = { viewModel.resetFilter() }
                        )


                    }
                }
            }
        }
    }

    @Composable
    fun NonRepetitionLayout(
        itemList: List<FilterItem>,
        onClick: (index: Int) -> Unit
    ) {
        Column(Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = R.string.crew_member_num),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            FlowRow(
                modifier = Modifier.padding(top = 12.dp),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 16.dp
            ) {
                itemList.forEachIndexed { index, item ->
                    RadioButton(
                        modifier = Modifier
                            .width(96.dp)
                            .height(48.dp),
                        checkedState = item.state,
                        text = item.name,
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
        onClick: () -> Unit
    ) {
        if (checkedState.value) checkedRadioButton = checkedState

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
                        onClick()
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
    fun FilterBottomButtons(
        modifier: Modifier,
        onApplyClick: () -> Unit,
        onResetClick: () -> Unit
    ) {
        Row(
            modifier = modifier
                .height(68.dp)
                .padding(vertical = 12.dp)
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .clickable(
                        onClick = onResetClick
                    ), contentAlignment = Alignment.Center


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
                    .height(44.dp),
                btnColor = R.color.main_orange,
                textString = R.string.apply, fontSize = 16.sp,
                onClick = onApplyClick
            )
        }
    }
}