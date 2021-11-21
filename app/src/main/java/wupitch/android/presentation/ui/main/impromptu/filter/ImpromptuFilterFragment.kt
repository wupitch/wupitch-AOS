package wupitch.android.presentation.ui.main.impromptu.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.impromptu.ImpromptuViewModel
import wupitch.android.util.TimeType

@AndroidEntryPoint
class ImpromptuFilterFragment : Fragment() {

    private var checkedRadioButton: MutableState<Boolean>? = null
    private val viewModel: ImpromptuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val scheduleList = listOf<FilterItem>(
                        FilterItem(
                            getString(R.string.in_one_week),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.in_two_weeks),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.in_three_weeks),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.in_four_weeks),
                            remember { mutableStateOf(false) })
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
                    val recruitSizeList = listOf<FilterItem>(
                        FilterItem(
                            getString(R.string.less_than_five),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.six_to_ten),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.eleven_to_fifteen),
                            remember { mutableStateOf(false) }),
                        FilterItem(
                            getString(R.string.sixteen_to_twenty),
                            remember { mutableStateOf(false) })
                    )

                    val scheduleState = remember { mutableStateOf(-1) }
                    val dayState = remember { mutableStateListOf<Int>() }
                    val recruitSizeState = remember { mutableStateOf(-1) }


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
                            }, textString = R.string.filter
                        ) { findNavController().navigateUp() }

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
                            NonRepetitionLayout(
                                itemList = scheduleList,
                                titleText = stringResource(id = R.string.schedule),
                            ) {
                                scheduleState.value = it
                               Log.d("{ImpromptuFilterFragment.onCreateView}", "일정 : ${scheduleState.value}")
                            }

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
                                itemList = recruitSizeList,
                                titleText = stringResource(id = R.string.num_of_recruitment),
                            ) {
                                recruitSizeState.value = it
                                Log.d("{ImpromptuFilterFragment.onCreateView}", "모집인원: ${recruitSizeState.value}")
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
    fun NonRepetitionLayout(
        titleText : String,
        itemList: List<FilterItem>,
        onClick: (index: Int) -> Unit
    ) {
        Column(Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = titleText,
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

//todo : refactoring & 초기화 & 적용하기 구현.
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