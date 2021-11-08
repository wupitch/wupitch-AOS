package wupitch.android.presentation.ui.components

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import wupitch.android.presentation.theme.Roboto

@Composable
fun WhiteRoundBtn(
    modifier: Modifier,
    textString : String,
    fontSize : TextUnit,
    @ColorRes textColor : Int,
    @ColorRes borderColor : Int,
    onClick : () ->Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = 1.dp, color = colorResource(
                    id = borderColor
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textString,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = textColor)
        )
    }
}