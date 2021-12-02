package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun MyCrewBoardTab() {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        val (chrt, text) = createRefs()
        val guildLine = createGuidelineFromTop(0.65f)

        Image(
            modifier = Modifier
                .constrainAs(chrt) {
                    bottom.linkTo(text.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(130.dp, 210.dp),
            painter = painterResource(id = R.drawable.img_chrt_02), contentDescription = null)

        Text(
            modifier = Modifier.constrainAs(text){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(guildLine)
            }.padding(top = 24.dp),
            text = stringResource(R.string.preparing),
            color = colorResource(id = R.color.gray02),
            fontFamily = Roboto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

    }

}