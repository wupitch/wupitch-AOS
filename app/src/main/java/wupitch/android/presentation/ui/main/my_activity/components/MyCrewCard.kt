package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import wupitch.android.R
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.SportKeyword
import wupitch.android.util.SportType

@Composable
fun MyCrewCard(
    crew: CrewCardInfo,
    onClick : (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(169.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.gray04))
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.gray01),
                    shape = RoundedCornerShape(16.dp)
                )
        )
        {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clickable {
                       onClick(0)
                    }

            ) {
                Image(
                    modifier = Modifier
                        .width(128.dp)
                        .fillMaxHeight(),
                    painter = if(crew.crewImage != null){
                        rememberImagePainter(
                            crew.crewImage,
                            builder = {
                                placeholder(R.color.gray04)
                                build()
                            }
                        )
                    } else painterResource(id = SportType.getNumOf(crew.sportId).thumbnailImage),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 11.dp, start = 12.dp, end = 12.dp)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (keyword) = createRefs()

                        SportKeyword(
                            modifier = Modifier
                                .constrainAs(keyword) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                }
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(id = SportType.getNumOf(crew.sportId).color))
                                .padding(horizontal = 8.dp, vertical = 1.dp),
                            sportName = SportType.getNumOf(crew.sportId).sportName
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = crew.title,
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
                            text = crew.time,
                            color = colorResource(id = R.color.gray05),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )
                        if (crew.isMoreThanOnceAWeek) {
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
                        text = crew.detailAddress,
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )
                }
            }

            Divider(
               color = colorResource(id = R.color.gray01),
                thickness = 1.dp
            )
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (board, firstDivider, gallery, secondDivider, members) = createRefs()

                Box(modifier = Modifier
                    .constrainAs(board) {
                        start.linkTo(parent.start)
                        end.linkTo(firstDivider.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .clickable {
                        onClick(1)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.board),
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_black)
                    )
                }


                Divider(
                    Modifier
                        .constrainAs(firstDivider) {
                            start.linkTo(board.end)
                            end.linkTo(gallery.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .height(14.dp)
                        .width(1.dp)
                        .background(colorResource(id = R.color.gray01)))

                Box(modifier = Modifier
                    .constrainAs(gallery) {
                        start.linkTo(firstDivider.end)
                        end.linkTo(secondDivider.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .clickable {
                        onClick(3)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.gallery),
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_black)
                    )

                }

                Divider(
                    Modifier
                        .constrainAs(secondDivider) {
                            start.linkTo(gallery.end)
                            end.linkTo(members.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .height(14.dp)
                        .width(1.dp)
                        .background(colorResource(id = R.color.gray01)))

                Box(modifier = Modifier
                        .constrainAs(members) {
                            start.linkTo(secondDivider.end)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .clickable {
                            onClick(2)
                        },
                contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.members),
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_black)
                    )
                }
            }
        }
    }

    Spacer(
        modifier = Modifier
            .height(10.dp)
            .fillMaxWidth()
    )
}