package wupitch.android.presentation.ui.main.my_activity.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.crew_detail.VisitorBottomSheetFragment
import wupitch.android.presentation.ui.main.home.crew_detail.components.JoinSuccessDialog
import wupitch.android.presentation.ui.main.home.crew_detail.components.NotEnoughInfoDialog
import wupitch.android.util.Sport

@Composable
fun MyCrewIntroTab() {

    lateinit var visitorBottomSheet: VisitorBottomSheetFragment


    WupitchTheme {
        val scrollState = rememberScrollState(0)


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)

        ) {
            Image(
                painter = painterResource(id = Sport.getNumOf(0).detailImage),
                contentDescription = "crew sport icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(202.dp),
                contentScale = ContentScale.Crop
            )
            CrewInfo()
            GrayDivider()

            CrewIntroCard()
            GrayDivider()

            CrewExtraInfo()
            GrayDivider()

            CrewGuidance()
        }
    }
}


@Composable
fun CrewExtraInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 20.dp, bottom = 24.dp)
            .padding(horizontal = 25.dp)
    ) {

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
            text = "준비물이 이 부분에 들어갑니다." + stringResource(id = R.string.medium_text),
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            lineHeight = 24.sp
        )
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
            text = "문의가 이 부분에 들어갑니다." + stringResource(id = R.string.medium_text),
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun CrewIntroCard() {
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
                text = "여기에 인원수가 들어갑니다.",
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
                text = "여기에 연령이 들어갑니다.",
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource
                    (id = R.color.main_black),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        CrewIntroKeyword()

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "크루 소개가 이 부분에 들어갑니다." + stringResource(id = R.string.long_text),
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun CrewIntroKeyword() {

    //todo : 소개 키워드 받기.
    val keywordList = listOf<String>(
        "크루 키워드",
        "이 부분에",
        "들어갑니다.",
        "초보 중심",
        "코치님과 훈련",
        "레슨 운영",
        "훈련 중심",
        "경기 중심"
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 24.dp),
        mainAxisSpacing = 12.dp,
        crossAxisSpacing = 14.dp
    ) {
        if (keywordList.isNotEmpty()) {
            keywordList.forEach {

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
fun CrewInfo() {
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
                .background(colorResource(id = Sport.getNumOf(0).color))
                .padding(horizontal = 13.dp, vertical = 4.dp),
            sportName = Sport.getNumOf(0).sportName
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "크루 이름이 이곳에 들어갑니다.",
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
                Text(
                    text = "수요일 20:00 - 22:00",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource
                        (id = R.color.main_black),
                    fontSize = 14.sp
                )
                Text(
                    text = "수요일 20:00 - 22:00",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource
                        (id = R.color.main_black),
                    fontSize = 14.sp
                )
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
                text = "구체적 위치가 여기에 들어갑니다.",
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
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_monetization_on),
                contentDescription = "won icon"
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                text = "정기회비 15,000",
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