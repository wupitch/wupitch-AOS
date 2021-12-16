package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import wupitch.android.R
import wupitch.android.domain.model.CrewMember
import wupitch.android.presentation.theme.Roboto

@Composable
fun CrewMemberLayout(
    member: CrewMember,
    onClick: (Int) -> Unit
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick(member.id) }

    ) {

        val (userImage, userName, leaderIcon, arrow, waiting) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(userImage) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(28.dp)
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
            modifier = Modifier.constrainAs(userName) {
                start.linkTo(userImage.end, margin = 6.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            text = member.userName,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(id = R.color.main_black)
        )

        if (member.isValid== false) {
            Box(modifier = Modifier
                .constrainAs(waiting) {
                    start.linkTo(userName.end, margin = 4.dp)
                    top.linkTo(userName.top)
                    bottom.linkTo(userName.bottom)
                }
                .clip(RoundedCornerShape(9.5.dp))
                .background(colorResource(id = R.color.gray03))
                .padding(horizontal = 6.dp)
                .padding(top = 2.dp, bottom = 1.dp),
                contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.waiting_for_acceptance),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }


        if (member.isLeader) {
            Image(
                modifier = Modifier
                    .constrainAs(leaderIcon) {
                        start.linkTo(userName.end)
                        top.linkTo(userName.top)
                        bottom.linkTo(userName.bottom)
                    }
                    .size(24.dp),
                painter = painterResource(id = R.drawable.btn_03_leader),
                contentDescription = null
            )
        }

        Image(
            modifier = Modifier
                .constrainAs(arrow) {
                    end.linkTo(parent.end, margin = 20.dp)
                    top.linkTo(userName.top)
                    bottom.linkTo(userName.bottom)
                }
                .size(24.dp),
            painter = painterResource(id = R.drawable.i_arrows_chevron_backward),
            contentDescription = null
        )
    }

}