package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import wupitch.android.R
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.viewModels
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.domain.model.AgeRadioButton
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.IconToolBar

@AndroidEntryPoint
class MyPageAgeGroupFragment : Fragment() {

    private val viewModel: MyPageAgeGroupViewModel by viewModels()
    private var checkedRadioButton: MutableState<Boolean>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserAgeGroup()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val checkedRadioButtonState = remember { mutableStateOf(false) }
                    val checkedState1 = remember { mutableStateOf(false) }
                    val checkedState2 = remember { mutableStateOf(false) }
                    val checkedState3 = remember { mutableStateOf(false) }
                    val checkedState4 = remember { mutableStateOf(false) }
                    val checkedState5 = remember { mutableStateOf(false) }

                    val ageList = arrayListOf<AgeRadioButton>(
                        AgeRadioButton("10대", checkedState1),
                        AgeRadioButton("20대", checkedState2),
                        AgeRadioButton("30대", checkedState3),
                        AgeRadioButton("40대", checkedState4),
                        AgeRadioButton("50대", checkedState5)
                    )

                    val updateState = remember { viewModel.updateState }
                    if (updateState.value.isSuccess) findNavController().navigateUp()
                    if (updateState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            updateState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val userAgeGroup = remember {viewModel.userAge}
                    if(userAgeGroup.value != null){
                        ageList[userAgeGroup.value!!].checkedState.value = true
                    }


                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = colorResource(
                                    id = R.color.white
                                )
                            )
                    ) {
                        val (toolbar, title, radioGroup, nextBtn, progressbar) = createRefs()

                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = { findNavController().navigateUp() })

                        Text(
                            modifier = Modifier
                                .constrainAs(title) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(toolbar.bottom, margin = 24.dp)
                                },
                            text = stringResource(id = R.string.select_age),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 32.sp
                        )

                        FlowRow(
                            modifier = Modifier
                                .constrainAs(radioGroup) {
                                    top.linkTo(title.bottom, margin = 32.dp)
                                    start.linkTo(title.start)
                                    end.linkTo(parent.end, margin = 20.dp)
//                                    width = Dimension.fillToConstraints
                                },
                            mainAxisSpacing = 16.dp,
                            crossAxisSpacing = 16.dp,
                            mainAxisSize = SizeMode.Wrap
                        ) {
                            ageList.forEach {
                                AgeRadioButton(
                                    modifier = Modifier
                                        .fillMaxWidth(0.42f)
//                                        .width(152.dp)
                                        .height(48.dp),
                                    checkedState = it.checkedState,
                                    textString = it.ageString,
                                    checkedRadioButtonState = checkedRadioButtonState
                                )
                            }
                        }

                        if(updateState.value.isLoading){
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(title.bottom)
                                    bottom.linkTo(nextBtn.top)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom, margin = 32.dp)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor = if(checkedRadioButtonState.value) R.color.main_orange else R.color.gray03,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            if(checkedRadioButtonState.value) {
                                var checkedAge = -1
                                ageList.forEachIndexed { index, ageRadioButton ->
                                    if(ageRadioButton.checkedState == checkedRadioButton)
                                        checkedAge = index
                                    return@forEachIndexed
                                }
                                viewModel.setUserAge(checkedAge)
                                viewModel.changeUserAgeGroup()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AgeRadioButton(
        modifier: Modifier,
        checkedState: MutableState<Boolean>,
        textString: String,
        checkedRadioButtonState : MutableState<Boolean>
    ) {
        if(checkedState.value) checkedRadioButton = checkedState

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
                        checkedRadioButtonState.value = true
                    }
                )
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (checkedState.value) colorResource(id = R.color.orange02) else
                        colorResource(id = R.color.gray01)
                )
                .border(
                    color = if (checkedState.value) colorResource(id = R.color.main_orange)
                    else androidx.compose.ui.graphics.Color.Transparent,
                    width = if (checkedState.value) 1.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textString,
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

