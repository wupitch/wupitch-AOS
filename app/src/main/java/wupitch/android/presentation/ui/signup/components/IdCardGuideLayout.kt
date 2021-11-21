package wupitch.android.presentation.ui.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun IdCardGuideLayout() {
    Column(Modifier.fillMaxWidth()) {
        GuideRow(
            guideNum = stringResource(id = R.string.one),
            guideText = stringResource(id = R.string.certify_idcard_guide1)
        )
        Spacer(modifier = Modifier.height(16.dp))
        GuideRow(
            guideNum = stringResource(id = R.string.two),
            guideText = stringResource(id = R.string.certify_idcard_guide2)
        )
        Spacer(modifier = Modifier.height(16.dp))
        GuideRow(
            guideNum = stringResource(id = R.string.three),
            guideText = stringResource(id = R.string.certify_idcard_guide3)
        )
    }
}


@Composable
fun GuideRow(
    guideNum: String,
    guideText: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(23.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = guideNum,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = guideText,
            color = colorResource(id = R.color.main_black),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Roboto
        )
    }
}