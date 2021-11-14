package wupitch.android.presentation.ui.main.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

class FilterFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         val sportsList = listOf<FilterItem>(
            FilterItem(getString(R.string.soccer_football)),
            FilterItem(getString(R.string.badminton)),
            FilterItem(getString(R.string.volleyball)),
            FilterItem(getString(R.string.basketball)),
            FilterItem(getString(R.string.hiking)),
            FilterItem(getString(R.string.running))
        )

         val dateList = listOf<FilterItem>(
            FilterItem(getString(R.string.monday)), FilterItem(getString(R.string.tuesday)),
            FilterItem(getString(R.string.wednesday)), FilterItem(getString(R.string.thursday)), FilterItem(getString(R.string.friday)),
            FilterItem(getString(R.string.saturday)), FilterItem(getString(R.string.sunday))
        )
         val ageGroupList = listOf<FilterItem>(
            FilterItem(getString(R.string.teenager)),
            FilterItem(getString(R.string.twenties)),
            FilterItem(getString(R.string.thirties)),
            FilterItem(getString(R.string.forties)),
            FilterItem(getString(R.string.over_fifties)),
            FilterItem(getString(R.string.regardless_of_age))
        )
         val crewSizeList = listOf<FilterItem>(
            FilterItem(getString(R.string.less_than_ten)),
            FilterItem(getString(R.string.more_than_ten_less_than_thirty)),
            FilterItem(getString(R.string.more_than_thirty_less_than_fifty)),
            FilterItem(getString(R.string.more_than_fifty_less_than_seventy)),
            FilterItem(getString(R.string.more_than_seventy))
        )

        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val eventState = remember {
                        mutableStateOf<MutableList<Int>>(mutableListOf())
                    }
                    val dayState = remember {
                        mutableStateOf<MutableList<Int>>(mutableListOf())
                    }
                    val ageGroupState = remember {
                        mutableStateOf<MutableList<Int>>(mutableListOf())
                    }
                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)) {
                        val scrollState = rememberScrollState(0)
                        val crewNumSelectedState = remember {
                            mutableStateOf(-1)
                        }

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
                                .verticalScroll(scrollState)
                                .padding(horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
//                            RepetitionLayout(
//                                text = R.string.event,
//                                filterItemList = sportsList,
//                                modifier = Modifier
//                                    .width(96.dp)
//                                    .height(48.dp),
//                                checkedListState = eventState
//                            )
//                            Spacer(modifier = Modifier.height(32.dp))
//                            RepetitionLayout(
//                                text = R.string.day,
//                                filterItemList = dateList,
//                                modifier = Modifier
//                                    .width(48.dp)
//                                    .height(48.dp),
//                                checkedListState = dayState
//                            )
//                            Spacer(modifier = Modifier.height(32.dp))
//                            TimeFilter()
//
//                            Spacer(modifier = Modifier.height(32.dp))
//                            NonRepetitionLayout(
//                                text = R.string.crew_member_num,
//                                filterItemList = crewSizeList,
//                                radioBtnModifier = Modifier
//                                    .width(96.dp)
//                                    .height(48.dp),
//                                flexBoxModifier = Modifier.padding(top = 12.dp),
//                                selectedState = crewNumSelectedState
//                            ){
//                                Log.d("{FilterFragment.onCreateView}", "크루원 수 : $it")
//                            }
//
//                            Spacer(modifier = Modifier.height(32.dp))
//                            RepetitionLayout(
//                                text = R.string.age_group,
//                                filterItemList = ageGroupList,
//                                modifier = Modifier
//                                    .width(96.dp)
//                                    .height(48.dp),
//                                checkedListState = ageGroupState
//                            )
//
//                            Spacer(modifier = Modifier.height(60.dp))



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
                        .background(
                            if (endTimeState.value == "00:00") colorResource(id = R.color.gray02)
                            else colorResource(id = R.color.main_orange)
                        )
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
            onClick = {
                val timeBottomSheet = TimeBottomSheetFragment()
                timeBottomSheet.show(childFragmentManager, "time bottom sheet fragment")
                Toast.makeText(requireContext(), "종료시간이 시작시간보다 늦어야 해요!", Toast.LENGTH_SHORT).show()
            }
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
       AndroidView(factory = { TimePicker(it) },
            Modifier.wrapContentSize(),
           update = { view ->
               view.setOnTimeChangedListener { view, hourOfDay, minute ->
                   Log.d("{FilterFragment.DatePicker}", "$hourOfDay $minute")
               }
           }
           )
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