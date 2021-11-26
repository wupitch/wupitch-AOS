package wupitch.android.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun ImpromptuGuidance() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 20.dp, bottom = 60.dp)
            .padding(horizontal = 25.dp)
    ) {

        Text(
            text = stringResource(id = R.string.guidance),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp
        )
        Row(Modifier.padding(top = 12.dp)) {
            Text(
                text = stringResource(id = R.string.impromptu_guide_dot),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )
            Text(
                text = stringResource(id = R.string.impromptu_guide1),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 22.sp
            )
        }
        Row(Modifier.padding(top = 22.dp)) {
            Text(
                text = stringResource(id = R.string.impromptu_guide_dot),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(stringResource(id = R.string.impromptu_guide2))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.impromptu_guide2_2))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(stringResource(id = R.string.impromptu_guide2_3))
                    }
                },
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 22.sp
            )
        }
        Row(Modifier.padding(top = 22.dp)) {
            Text(
                text = stringResource(id = R.string.impromptu_guide_dot),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )
            Text(
                text = stringResource(id = R.string.impromptu_guide3),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 22.sp
            )
        }
        Row(Modifier.padding(top = 22.dp)) {
            Text(
                text = stringResource(id = R.string.impromptu_guide_dot),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )
            Text(
                text = stringResource(id = R.string.impromptu_guide4),
                fontSize = 14.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 22.sp
            )
        }
    }
}