package wupitch.android.presentation.ui.components

import android.util.Log
import android.view.WindowInsets
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun ToggleBtn(
    index : Int = 0,
    toggleState: MutableState<Boolean>,
    modifier: Modifier,
    textString: String,
    checkedListState : SnapshotStateList<Int> = mutableStateListOf<Int>(),
    onCheckedChange: (Boolean) -> Unit
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))

            .background(
                if (toggleState.value) colorResource(id = R.color.orange02) else
                    colorResource(id = R.color.gray01)
            )
            .border(
                color = if (toggleState.value) colorResource(id = R.color.main_orange)
                else Color.Transparent,
                width = if (toggleState.value) 1.dp else 0.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .toggleable(
                value = toggleState.value,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = true,
                role = Role.Checkbox,
                onValueChange = {
                    
                    toggleState.value = it
//                    onCheckedChange(it)
                    if (it) {
                        if(!checkedListState.contains(index)) {
                            checkedListState.add(index)
                        }
                    }else {
                        if(checkedListState.contains(index)) {
                            checkedListState.remove(index)
                        }
                    }
                    checkedListState.forEach{ item ->
                        Log.d("CreateCrewInfoFragment", item.toString())
                    }
                    Log.d("CreateCrewInfoFragment", toggleState.value.toString())
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textString,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = if (toggleState.value) colorResource(id = R.color.main_orange)
            else colorResource(id = R.color.gray02)
        )
    }
}