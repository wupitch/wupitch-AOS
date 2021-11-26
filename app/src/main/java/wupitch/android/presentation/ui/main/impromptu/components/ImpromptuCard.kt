package wupitch.android.presentation.ui.main.impromptu.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.presentation.theme.Roboto
import wupitch.android.util.Sport

@Composable
fun ImpromptuCard(
    cardInfo: ImpromptuCardInfo,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(142.dp)
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
                    .background(colorResource(id = R.color.main_orange))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_bungae),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 11.dp, start = 12.dp, end = 12.dp)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (remainingDays, pin) = createRefs()

                    RemainingDays(
                        modifier = Modifier
                            .constrainAs(remainingDays) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            },
                        remainingDays = cardInfo.remainingDays
                    )
                    if (cardInfo.isPinned) {
                        Image(
                            modifier = Modifier.constrainAs(pin) {
                                top.linkTo(remainingDays.top)
                                bottom.linkTo(remainingDays.bottom)
                                end.linkTo(parent.end)
                            },
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "pin"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = cardInfo.title,
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(Modifier.padding(vertical = 3.dp)) {
                        Text(
                            text = "21.00.00",
                            color = colorResource(id = R.color.gray05),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )

                    Text(
                        text = cardInfo.time,
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = "20:00",
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = cardInfo.detailAddress,
                    color = colorResource(id = R.color.gray05),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.user_circle),
                        contentDescription = null
                    )
                    Text(
                        text = "${cardInfo.gatheredPeople}/${cardInfo.totalCount}",
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
    }

}