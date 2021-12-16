package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.domain.model.MemberDetail
import wupitch.android.presentation.theme.Roboto
import wupitch.android.util.SportType

@Composable
fun MemberInfoLayout(
    modifier: Modifier,
    member: MemberDetail
) {
    ConstraintLayout(modifier.fillMaxWidth()) {
        val (image, nickname, info, sports, intro, visitorInfo) = createRefs()
        Image(
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top)
                }
                .size(64.dp)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.gray01),
                    shape = CircleShape
                ),
            painter = if (member.userImage != null) {
                rememberImagePainter(
                    member.userImage,
                    builder = {
                        placeholder(R.drawable.profile_basic)
                        transformations(CircleCropTransformation())
                        build()
                    }
                )
            } else painterResource(id = R.drawable.profile_basic),
            contentDescription = null
        )
        Text(
            modifier = Modifier.constrainAs(nickname) {
                start.linkTo(image.end, margin = 16.dp)
                top.linkTo(parent.top, margin = 8.dp)
            },
            text = member.userName,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(id = R.color.main_black)
        )

        Row(modifier = Modifier.constrainAs(info) {
            top.linkTo(nickname.bottom, margin = 2.dp)
            start.linkTo(nickname.start)
        }, verticalAlignment = Alignment.CenterVertically) {
            InfoText(textString = member.userAgeGroup)
            InfoDotText()
            InfoText(textString = member.userArea)
            InfoDotText()
            InfoText(textString = member.userPhoneNum)
        }

        FlowRow(
            modifier = Modifier
                .constrainAs(sports) {
                    start.linkTo(parent.start, margin = 24.dp)
                    top.linkTo(image.bottom, margin = 20.dp)
                }
                .fillMaxWidth()
                .padding(end = 80.dp),
            mainAxisSpacing = 10.dp,
            crossAxisSpacing = 10.dp
        ) {
            member.userSports.forEach {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(id = SportType.getNumOf(it.sportsId).color))
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = it.name,
                        color = Color.White,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        Box(modifier = Modifier
            .constrainAs(intro) {
                start.linkTo(parent.start)
                top.linkTo(sports.bottom, margin = 32.dp)
            }
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(164.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.gray04))
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 11.dp),
                text = member.intro,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = colorResource(id = R.color.main_black)
            )
        }

        if(member.visitorDate != null) {
            Box(modifier = Modifier
                .constrainAs(visitorInfo) {
                    start.linkTo(parent.start)
                    top.linkTo(intro.bottom, margin = 32.dp)
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = R.color.orange02)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.visitorDate,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.main_orange)
                )
            }
        }
    }
}

@Composable
fun InfoText(
    textString: String
) {
    Text(
        text = textString,
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colorResource(id = R.color.gray02)
    )
}

@Composable
fun InfoDotText() {
    Text(
        text = stringResource(id = R.string.impromptu_guide_dot),
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colorResource(id = R.color.gray02)
    )
}