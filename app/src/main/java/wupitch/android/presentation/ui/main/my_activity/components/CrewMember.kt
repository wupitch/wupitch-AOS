package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.main.my_activity.my_crew.Member

@Composable
fun CrewMember( //todo 게시판, 번개 멤버 리팩토링
    member : Member,
    onClick : (Int) -> Unit
){
    ConstraintLayout(
        Modifier.fillMaxWidth().height(60.dp)
            .clickable { onClick(member.id) }

    ) {

        val (userImage, userName, leaderIcon) = createRefs()

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

        if(member.isLeader){
            Image(
                modifier = Modifier
                    .constrainAs(leaderIcon) {
                        start.linkTo(userName.end)
                        top.linkTo(userName.top)
                        bottom.linkTo(userName.bottom)
                    }.size(24.dp),
                painter = painterResource(id = R.drawable.btn_03_leader),
                contentDescription = null
            )
        }
    }

}