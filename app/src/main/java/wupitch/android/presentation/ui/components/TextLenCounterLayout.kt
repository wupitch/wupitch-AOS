package wupitch.android.presentation.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun TextLenCounterLayout (
    modifier: Modifier,
    textState : MutableState<String>,
    maxLength : Int
) {
    Text(
        modifier = modifier,
        text = "${textState.value.length}/$maxLength",
        color = colorResource(
            id = R.color.gray05
        ),
        fontSize = 12.sp,
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal
    )
}