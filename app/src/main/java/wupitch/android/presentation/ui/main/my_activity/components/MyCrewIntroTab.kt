package wupitch.android.presentation.ui.main.my_activity.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.crew_detail.VisitorBottomSheetFragment
import wupitch.android.presentation.ui.main.home.crew_detail.components.JoinSuccessDialog
import wupitch.android.presentation.ui.main.home.crew_detail.components.NotEnoughInfoDialog
import wupitch.android.presentation.ui.main.my_activity.MyCrewViewModel
import wupitch.android.util.Sport

@Composable
fun MyCrewIntroTab(
    viewModel: MyCrewViewModel = hiltViewModel()
) {

    viewModel.getCrewDetail()
    WupitchTheme {
        val scrollState = rememberScrollState(0)

        val crewState = remember { viewModel.crewDetailState }
        if (crewState.value.error.isNotEmpty()) {
            Log.d("{MyCrewIntroTab}", crewState.value.error)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val ( crewInfoCol, progressbar) = createRefs()

            crewState.value.data?.let { crewInfo ->

                Column(
                    modifier = Modifier.constrainAs(crewInfoCol) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                        .fillMaxSize()
                        .verticalScroll(scrollState)

                ) {
                    Image(
                        painter = if (crewInfo.crewImage != null) {
                            rememberImagePainter(
                                crewInfo.crewImage,
                                builder = {
                                    placeholder(Sport.getNumOf(crewInfo.sportsId).detailImage)
                                    build()
                                }
                            )
                        } else {
                            rememberImagePainter(Sport.getNumOf(crewInfo.sportsId).detailImage)
                        },
                        contentDescription = "crew sport icon",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(202.dp),
                        contentScale = ContentScale.Crop
                    )
                    CrewInfo(crewInfo)
                    GrayDivider()

                    CrewIntroCard(crewInfo)
                    GrayDivider()

                    CrewExtraInfo(crewInfo)
                    GrayDivider()

                    CrewGuidance()
                }
            }

            if (crewState.value.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progressbar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    color = colorResource(id = R.color.main_orange)
                )
            }
        }

    }
}


@Composable
fun CrewExtraInfo(
    crewState: CrewDetailResult
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 20.dp, bottom = 24.dp)
            .padding(horizontal = 25.dp)
    ) {

         if(crewState.materials != null) {
                Text(
                    text = stringResource(id = R.string.supplies),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.main_black),
                    fontSize = 16.sp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 36.dp),
                    text = crewState.materials,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
                    lineHeight = 24.sp
                )
            }
        VisitorDefLayout(Modifier)
        Text(
            modifier = Modifier.padding(top = 36.dp),
            text = stringResource(id = R.string.inquiry),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            text = crewState.inquiries ?: "",
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun CrewIntroCard(
    crewState: CrewDetailResult
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 20.dp, bottom = 24.dp)
            .padding(horizontal = 25.dp)
    ) {

        Text(
            text = stringResource(id = R.string.introduction),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            Text(
                text = stringResource(id = R.string.persons),
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
            Text(
                text = crewState.memberCount,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        ) {
            Text(
                text = stringResource(id = R.string.age),
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
            Text(
                text =crewState.ageTable,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        CrewIntroKeyword(crewState)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = crewState.introduction,
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun CrewIntroKeyword(
    crewState: CrewDetailResult
) {

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 24.dp),
        mainAxisSpacing = 12.dp,
        crossAxisSpacing = 14.dp
    ) {
        if (crewState.extraList.isNotEmpty()) {
            crewState.extraList.forEach {

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(id = R.color.gray01))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = it,
                        color = colorResource(id = R.color.gray05),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }
            }
        }

    }
}

@Composable
fun CrewInfo(
    crewState: CrewDetailResult
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            )
    ) {

        SportKeyword(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(colorResource(id = Sport.getNumOf(crewState.sportsId).color))
                .padding(horizontal = 13.dp, vertical = 4.dp),
            sportName = Sport.getNumOf(crewState.sportsId).sportName
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = crewState.crewName,
            fontSize = 18.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 1
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_date_fill),
                contentDescription = "calendar icon"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                crewState.schedules.forEach { item ->
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = item,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        color = colorResource
                            (id = R.color.main_black),
                        fontSize = 14.sp
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_pin_fill),
                contentDescription = "location icon"
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                text = crewState.areaName,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        if (crewState.dues.isNotEmpty()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_monetization_on),
                    contentDescription = "won icon"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    crewState.dues.forEach { item ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            text = item,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            color = colorResource
                                (id = R.color.main_black),
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}