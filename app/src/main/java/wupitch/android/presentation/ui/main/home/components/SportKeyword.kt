package wupitch.android.presentation.ui.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.presentation.theme.Roboto

@Composable
fun SportKeyword (
    modifier: Modifier,
    sportName : Int,
    sportColor : Int
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = sportColor))
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = stringResource(id = sportName),
            color = Color.White,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
    }
}
