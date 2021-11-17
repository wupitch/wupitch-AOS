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
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.SportKeyword
import wupitch.android.util.Sport

@Composable
fun CrewCard(
    crewCard: CrewCardInfo,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier
            .height(8.dp)
            .fillMaxWidth())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(126.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.gray04))
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.gray01),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClick() }

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
                    .padding(top = 11.dp, start = 12.dp, end = 12.dp)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (keyword, pin) = createRefs()

                    SportKeyword(
                        modifier = Modifier
                            .constrainAs(keyword) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(id = Sport.BASKETBALL.color))
                            .padding(horizontal = 8.dp, vertical = 1.dp),
                        sportName = Sport.BASKETBALL.sportName
                    )
                    if (crewCard.isPinned) {
                        Image(
                            modifier = Modifier.constrainAs(pin) {
                                top.linkTo(keyword.top)
                                bottom.linkTo(keyword.bottom)
                                end.linkTo(parent.end)
                            },
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "pin"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = crewCard.name,
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(Modifier.padding(vertical = 3.dp)) {
                    if (crewCard.isBiweekly) {
                        Text(
                            text = stringResource(id = R.string.biweekly),
                            color = colorResource(id = R.color.gray05),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )
                    }

                    Text(
                        text = crewCard.time,
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )

                    if (crewCard.isMoreThanOnceAWeek) {
                        Text(
                            text = stringResource(id = R.string.moreThanOnceAWeek),
                            color = colorResource(id = R.color.gray05),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = crewCard.detailAddress,
                    color = colorResource(id = R.color.gray05),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
//                Spacer(modifier = Modifier.height(17.dp))



            }
        }

        Spacer(modifier = Modifier
            .height(10.dp)
            .fillMaxWidth())
    }

}