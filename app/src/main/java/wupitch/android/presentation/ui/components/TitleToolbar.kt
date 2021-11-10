package wupitch.android.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun TitleToolbar(
    modifier: Modifier,
    @StringRes textString: Int,
    onLeftIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(modifier = Modifier
            .size(24.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null)
            { onLeftIconClick() },
            painter = painterResource(id = R.drawable.left),
            contentDescription = "go back previous page"
        )

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = stringResource(id = textString),
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )


    }
}