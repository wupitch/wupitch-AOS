package wupitch.android.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.util.TimeType

@Composable
fun TimeFilter(
    startTimeState: State<String>,
    endTimeState: State<String>,
    hasStartTimeSet: State<Boolean?>,
    hasEndTimeSet: State<Boolean?>,
    onClick : (TimeType) -> Unit
) {


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
            TimeButton(startTimeState, TimeType.START, hasStartTimeSet, onClick)
            Spacer(modifier = Modifier.width(4.dp))
            Divider(
                Modifier
                    .width(8.dp)
                    .height(1.dp)
                    .background(
                        if (hasEndTimeSet.value == true) colorResource(id = R.color.main_orange)
                        else colorResource(id = R.color.gray02)
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            TimeButton(endTimeState, TimeType.END, hasEndTimeSet, onClick)
        }
    }
}


@Composable
private fun TimeButton(
    timeState: State<String>,
    timeType: TimeType,
    hasSetTimeState: State<Boolean?>,
    onClick: (TimeType) -> Unit
) {

    Button(
        modifier = Modifier
            .width(96.dp)
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (hasSetTimeState.value == true) colorResource(id = R.color.main_orange)
            else colorResource(id = R.color.gray02)
        ),
        colors = ButtonDefaults.buttonColors(Color.White),
        elevation = null,
        onClick = { onClick(timeType) }
    ) {
        Text(
            text = timeState.value,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = if (hasSetTimeState.value == true) colorResource(id = R.color.main_orange)
            else colorResource(id = R.color.gray02)
        )
    }
}