package wupitch.android.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
fun SetAppBar(
    modifier: Modifier,
    onClick: () -> Unit,
    textString: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(modifier = Modifier
            .size(24.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null)
            //interaction source??
            { onClick() },
            painter = painterResource(id = R.drawable.left),
            contentDescription = "go back previous page"
        )


        Text(
            modifier = modifier.padding(start = 32.dp),
            text = stringResource(id = textString),
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

    }

}