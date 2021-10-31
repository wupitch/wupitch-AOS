package wupitch.android.presentation.ui.main.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.data.remote.CrewCardInfo
import wupitch.android.presentation.theme.Roboto
import wupitch.android.util.Sport

@Composable
fun CrewCard(
    crewCard : CrewCardInfo,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = 17.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.gray04))
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.gray01),
                shape = RoundedCornerShape(16.dp)
            ).clickable { onClick() }

    ) {
        Box(
            modifier = Modifier
                .width(128.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                .background(colorResource(id = Sport.BASKETBALL.color))
        ) {
            Image(
                painter = painterResource(id = Sport.BASKETBALL.icon),
                contentDescription = "",
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, start = 12.dp, end = 10.dp, bottom = 14.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (keyword, location, pin) = createRefs()

                SportKeyword(
                    modifier = Modifier.constrainAs(keyword) {
                        top.linkTo(pin.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(pin.bottom)
                    },
                    sportName = Sport.BASKETBALL.sportName,
                    sportColor = Sport.BASKETBALL.color
                )
                Text(
                    modifier = Modifier
                        .width(59.dp) //todo : maxLength 함수 만들기.
                        .constrainAs(location) {
                            top.linkTo(pin.top)
                            bottom.linkTo(pin.bottom)
                            start.linkTo(keyword.end)
                        }
                        .padding(start = 8.dp),
                    text = crewCard.location,
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if(crewCard.isPinned) {
                    Image(
                        modifier = Modifier.constrainAs(pin) {
                            top.linkTo(keyword.top)
                            bottom.linkTo(keyword.bottom)
                            end.linkTo(parent.end)
                        },
                        painter = painterResource(id = R.drawable.ic_pin), contentDescription = "pin")
                }

            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = crewCard.name,
                color = Color.Black,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(modifier = Modifier.padding(top=4.dp)) {
                if(crewCard.isBiweekly) {
                    Text(
                        text = stringResource(id = R.string.biweekly),
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp)
                }

                Text(text = crewCard.time,
                    color = colorResource(id = R.color.gray05),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp)

                if(crewCard.isMoreThanOnceAWeek) {
                    Text(text = stringResource(id = R.string.moreThanOnceAWeek),
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp)
                }
            }

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = crewCard.detailAddress,
                color = colorResource(id = R.color.gray05),
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )





        }
    }


}