package wupitch.android.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun VisitorDefLayout (
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.orange03))
            .padding(top = 16.dp, bottom = 28.dp)
            .padding(horizontal = 20.dp)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.what_is_visitors),
                color = colorResource(id = R.color.main_orange),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = stringResource(id = R.string.visitor_explanation),
                color = colorResource(id = R.color.main_orange),
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}