package wupitch.android.presentation.ui.main.my_page.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.presentation.theme.Roboto

@Composable
fun MyPageText(
    modifier: Modifier,
    textString: String,
    onClick : () -> Unit
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onClick()
            },
        text = textString,
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black,
        textAlign = TextAlign.Start
    )
}