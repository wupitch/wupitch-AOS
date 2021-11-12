package wupitch.android.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto

@Composable
fun NonRepetitionLayout(
    text: Int? = null,
    filterItemList: List<FilterItem>,
    flexBoxModifier : Modifier,
    radioBtnModifier: Modifier,
    selectedState : MutableState<Int>,
    onClick: (index: Int) -> Unit
) {
    var checkedRadioButton: MutableState<Boolean>? = null

    Column(Modifier.fillMaxWidth()) {
        if(text != null){
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(id = text),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        FlowRow(
            modifier = flexBoxModifier,
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {


            filterItemList.forEachIndexed { index, item ->
//                RadioButton(
//                    modifier = radioBtnModifier,
//                    checkedState = item.state,
//                    text = item.name,
//                    index = index,
//                    checkedRadioButton = checkedRadioButton
//                ) {
//                    Log.d("{FilterFragment.NonRepetitionFilter}", "크루원 수 : $it")
//                }

                Box(
                    modifier = radioBtnModifier
                        .selectable(
                            selected = item.state.value,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = true,
                            role = Role.RadioButton,
                            onClick = {
                                checkedRadioButton?.value = false
                                item.state.value = true
                                checkedRadioButton = item.state
                                selectedState.value = index
                            }
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (item.state.value) colorResource(id = R.color.orange02) else
                                colorResource(id = R.color.gray01)
                        )
                        .border(
                            color = if (item.state.value) colorResource(id = R.color.main_orange)
                            else Color.Transparent,
                            width = if (item.state.value) 1.dp else 0.dp,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.name,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        color = if (item.state.value) colorResource(id = R.color.main_orange)
                        else colorResource(id = R.color.gray02)
                    )
                }




            }
        }
    }
}


//@Composable
//fun RadioButton(
//    modifier: Modifier,
//    checkedState: MutableState<Boolean>,
//    text: Int,
//    index: Int,
//    checkedRadioButton: MutableState<Boolean>? = null,
//    onClick: (index: Int) -> Unit
//) {
//    Box(
//        modifier = modifier
//            .selectable(
//                selected = checkedState.value,
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null,
//                enabled = true,
//                role = Role.RadioButton,
//                onClick = {
//                    checkedRadioButton?.value = false
//                    checkedState.value = true
//                    checkedRadioButton = checkedState
//                    onClick(index)
//                }
//            )
//            .clip(RoundedCornerShape(8.dp))
//            .background(
//                if (checkedState.value) colorResource(id = R.color.orange02) else
//                    colorResource(id = R.color.gray01)
//            )
//            .border(
//                color = if (checkedState.value) colorResource(id = R.color.main_orange)
//                else Color.Transparent,
//                width = if (checkedState.value) 1.dp else 0.dp,
//                shape = RoundedCornerShape(8.dp)
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = stringResource(id = text),
//            textAlign = TextAlign.Center,
//            fontSize = 14.sp,
//            fontFamily = Roboto,
//            fontWeight = FontWeight.Bold,
//            color = if (checkedState.value) colorResource(id = R.color.main_orange)
//            else colorResource(id = R.color.gray02)
//        )
//    }
//}