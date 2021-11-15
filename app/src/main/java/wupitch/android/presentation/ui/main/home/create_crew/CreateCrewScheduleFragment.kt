package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.FullToolBar
import wupitch.android.presentation.ui.components.NoToggleLayout
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.StopWarningDialog

class CreateCrewScheduleFragment : Fragment() {

    private var checkedRadioButton: MutableState<Boolean>? = null


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

                    val firstBtnToggleState = remember{ mutableStateOf(false)}
                    val secondBtnToggleState = remember{ mutableStateOf(false)}

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
                                text = stringResource(id = R.string.crew_schedule),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                                lineHeight = 28.sp
                            )

                            ScheduleLayout()
                            PlusButton(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                toggleState = firstBtnToggleState
                            )
                            if(firstBtnToggleState.value) {
                                ScheduleLayout()
                                PlusButton(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    toggleState = secondBtnToggleState
                                )
//                                SideEffect {
//                                    scope.launch {
//                                        //todo 화면 끝까지 스크롤하기.
//                                        scrollState.animateScrollTo(600)
//                                    }
//                                }
                            }
                            if(secondBtnToggleState.value){
                                ScheduleLayout()

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
                            btnColor = R.color.gray03,
                            textString = R.string.four_over_seven,
                            fontSize = 16.sp
                        ) {
                            findNavController().navigate(R.id.action_createCrewScheduleFragment_to_createCrewImageFragment)

                        }
                    }
                }
            }
        }
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
            ), contentAlignment = Alignment.Center){
            Icon(
                painter = if(toggleState.value) painterResource(id = R.drawable.ic_subtract)
                else painterResource(id = R.drawable.ic_btn_06_add),
                contentDescription = "plus icon",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(48.dp))

    }

    @Composable
    private fun ScheduleLayout() {

        val dayList = listOf<FilterItem>(
            FilterItem(stringResource(id = R.string.monday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.tuesday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.wednesday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.thursday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.friday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.saturday), remember { mutableStateOf(false) }),
            FilterItem(stringResource(id = R.string.sunday), remember { mutableStateOf(false) })
        )

        val dayState = remember { mutableStateOf(-1)}
        val toggleState = remember { mutableStateOf(false)}

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.day),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        NonRepetitionLayout(
            itemList = dayList
        ){
            dayState.value = it
        }

        Spacer(modifier = Modifier.height(16.dp))
        NoToggleLayout(toggleState = toggleState, textString = stringResource(id = R.string.every_other_week))

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.time),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
    }

    @Composable
    fun NonRepetitionLayout(
        itemList: List<FilterItem>,
        onClick: (index: Int) -> Unit
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            itemList.forEachIndexed { index, item ->
                RadioButton(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    checkedState = item.state,
                    text = item.name,
                ) {
                    onClick(index)
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
}