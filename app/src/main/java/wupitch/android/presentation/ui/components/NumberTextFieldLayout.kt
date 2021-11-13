package wupitch.android.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.NumberTextField

@ExperimentalPagerApi
@Composable
fun NumberTextFieldLayout(
    modifier: Modifier,
    textState : MutableState<String>,
    measureString : String,
    thousandIndicator : Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NumberTextField(
            modifier = modifier,
            textState = textState,
            thousandIndicator = thousandIndicator
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = measureString,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )

    }
}