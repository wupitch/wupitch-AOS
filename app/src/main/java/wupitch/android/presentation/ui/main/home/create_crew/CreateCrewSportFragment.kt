package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.StopWarningDialog
import wupitch.android.presentation.ui.components.TitleToolbar

@AndroidEntryPoint
class CreateCrewSportFragment : Fragment() {


    private val viewModel: CreateCrewViewModel by activityViewModels()
    private var checkedRadioButton: MutableState<Boolean>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {


            setContent {

                val stopSignupState = remember { mutableStateOf(false) }
                val dialogOpenState = remember { mutableStateOf(false) }

                if (stopSignupState.value) { findNavController().navigateUp() }
                if (dialogOpenState.value) {
                    StopWarningDialog(
                        dialogOpenState = dialogOpenState,
                        stopSignupState = stopSignupState,
                        textString = stringResource(id = R.string.stop_create_crew_warning)
                    )
                }

                val sportSelectedState = remember { viewModel.crewSportId }

                BackHandler {
                    if (viewModel.userDistrictId.value != null) dialogOpenState.value = true
                    else findNavController().navigateUp()
                }

                val sportsList = viewModel.sportsList.value

                ConstraintLayout(
                    Modifier
                        .background(Color.White)
                        .fillMaxSize()
                ) {
                    val (toolbar, divider, content, nextBtn, progressBar) = createRefs()

                    TitleToolbar(modifier = Modifier.constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, textString = R.string.create_crew) {
                        Log.d(
                            "{CreateCrewSportFragment.onCreateView}",
                            viewModel.userDistrictId.value.toString()
                        )
                        if (viewModel.userDistrictId.value != null) {
                            dialogOpenState.value = true
                        } else {
                            findNavController().navigateUp()
                        }
                    }

                    Divider(
                        Modifier
                            .constrainAs(divider) {
                                top.linkTo(toolbar.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                            .width(1.dp)
                            .background(colorResource(id = R.color.gray01)))

                    if (sportsList.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.constrainAs(progressBar) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(toolbar.bottom)
                                bottom.linkTo(nextBtn.top)
                            },
                            color = colorResource(id = R.color.main_orange)
                        )
                    }

                    if (sportsList.data.isNotEmpty()) {
                        val sportRememberList = mutableListOf<FilterItem>()

                        sportsList.data.forEach {
                            if(it.state.value){
                                sportRememberList.add(FilterItem(it.name, remember {
                                    mutableStateOf(true)
                                }))
                            }else {
                                 sportRememberList.add(FilterItem(it.name, remember {
                                mutableStateOf(false)
                            }))
                            }
                        }

                        //아래가 여러번 불림. 왜???  sportsList.data 가 변하는 것도 아닌데...
                        // 아마도 sportsList 에 state 가 있고, 그 state 가
                        //클릭에 따라 변하기 때문에 여러번 invoke 되는 것이 아닐까 싶은데...

                        //그런데 다른 파일에 있을 때와 위와 같은데, 한 파일에 있을 때는 여러번 불리지 않는다..
                        //state 가 바뀌는 건 같은데... 도대체 무슨 일일까.,,???
                        Log.d(
                            "{CreateCrewSportFragment.onCreateView}",
                            sportsList.data.map { it.state }.toString()
                        )

                        Column(Modifier.constrainAs(content) {
                            top.linkTo(divider.bottom, margin = 24.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                        }) {
                            Text(
                                text = stringResource(id = R.string.select_crew_sport),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                                lineHeight = 28.sp
                            )
                            NonRepetitionLayout(
                                filterItemList = sportRememberList.toList(),
                                flexBoxModifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                radioBtnModifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp),
                            ) {
                                //한 파일안 에 있고 아래 state 코드가 없으면 이 if문 이 여러번 불리지 않는다.
                                //그런데 아래 state 가 바뀌면 if문 이 여러번 호출된다.
                                //결론 : state 가 사용되는 곳은, state 가 바뀌면 호출된다.
                                viewModel.setCrewSport(it)
                                Log.d(
                                    "{CreateCrewSport.onCreateView}",
                                    "스포츠 : ${sportSelectedState.value} "
                                )
                            }
                        }
                    }

                    //todo 동 state 추가.
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
                        btnColor = if (sportSelectedState.value != -1) R.color.main_orange
                        else R.color.gray03,
                        textString = R.string.one_over_seven,
                        fontSize = 16.sp
                    ) {
                        if (sportSelectedState.value != -1) {
                            Log.d("{CreateCrewSport.onCreateView}", "next btn clicked!")

                            viewModel.setCrewSport(sportSelectedState.value)
                            findNavController().navigate(R.id.action_createCrewSport_to_createCrewLocationFragment)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NonRepetitionLayout(
        filterItemList: List<FilterItem>,
        flexBoxModifier: Modifier,
        radioBtnModifier: Modifier,
        onClick: (index: Int) -> Unit
    ) {

        FlowRow(
            modifier = flexBoxModifier,
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            filterItemList.forEachIndexed { index, item ->
                RadioButton(
                    modifier = radioBtnModifier,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSports()
    }
}