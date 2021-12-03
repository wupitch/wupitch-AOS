package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.GrayDivider
import wupitch.android.presentation.ui.main.my_activity.my_crew.CrewPost

@Composable
fun MyCrewPostCard(
    post: CrewPost,
    onMoreClick: () -> Unit,
    onLikeClick: (id: Int) -> Unit
) {

    val toggleState = remember { mutableStateOf(post.isLiked)}
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        if (post.isAnnounce) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (announceChip, announcement, more) = createRefs()

                Box(modifier = Modifier
                    .constrainAs(announceChip) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 20.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.main_orange),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 1.dp),
                        text = stringResource(id = R.string.announcement_post),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.main_orange)
                    )
                }
                Text(
                    modifier = Modifier.constrainAs(announcement) {
                        start.linkTo(announceChip.end, margin = 8.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    text = post.announceTitle!!,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.main_black)
                )

                Icon(modifier = Modifier
                    .constrainAs(more) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 20.dp)
                    }
                    .size(24.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onMoreClick
                    ),
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(17.dp))
            Row(
                modifier = Modifier.padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(28.dp)
                        .border(width = 1.dp, color = colorResource(id = R.color.gray01), shape = CircleShape),
                    painter = if (post.userImage != null) {
                        rememberImagePainter(
                            post.userImage,
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
                    modifier = Modifier.padding(start = 6.dp),
                    text = post.userName,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.main_black)
                )

                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.btn_03_leader),
                    contentDescription = null
                )
            }

        } else {
            ConstraintLayout(Modifier.fillMaxWidth()) {

                val (userImage, userName, more) = createRefs()

                Image(
                    modifier = Modifier
                        .constrainAs(userImage) {
                            start.linkTo(parent.start, margin = 20.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(28.dp)
                        .border(width = 1.dp, color = colorResource(id = R.color.gray01), shape = CircleShape),
                    painter = if (post.userImage != null) {
                        rememberImagePainter(
                            post.userImage,
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
                    text = post.userName,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.main_black)
                )

                Image(modifier = Modifier
                    .constrainAs(more) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 20.dp)
                    }
                    .size(24.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onMoreClick
                    ),
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = post.content,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = colorResource(id = R.color.main_black)
        )
        Spacer(modifier = Modifier.height(9.dp))

        ConstraintLayout(Modifier.fillMaxWidth()) {

            val (like, date) = createRefs()

            Row(modifier = Modifier
                .constrainAs(like) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .toggleable(
                    value = toggleState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.Checkbox,
                    onValueChange = {
                        toggleState.value = it
                        onLikeClick(post.id)
                    }
                ), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = if(toggleState.value) painterResource(id = R.drawable.react_heart) else painterResource(id = R.drawable.react_heart_inact),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    text = post.likedNum.toString(),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (toggleState.value) colorResource(id = R.color.pink) else colorResource(id = R.color.gray03)
                )
            }

            Text(
                modifier = Modifier.constrainAs(date){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 20.dp)
                },
                text = post.date,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray08)
            )

        }
        Spacer(modifier = Modifier.height(17.dp))

        GrayDivider()

    }
}