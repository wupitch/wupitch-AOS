package wupitch.android.presentation.ui.main.crew_detail

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.crew_detail.components.JoinCrewDialog
import wupitch.android.presentation.ui.main.crew_detail.components.NotEnoughInfoDialog
import wupitch.android.util.Sport

class CrewDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("crewId")?.let { crewId ->
//            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
            //todo : get crew from viewModel with the id.
            Log.d("{CrewDetailFragment.onCreate}", crewId.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val scrollState = rememberScrollState(0)
                val joinVisitorDialogOpenState = remember {
                    mutableStateOf(false)
                }
                val joinDialogOpenState = remember {
                    mutableStateOf(false)
                }
                val notEnoughInfoDialogOpenState = remember {
                    mutableStateOf(false)
                }

                if (joinVisitorDialogOpenState.value)
                    JoinCrewDialog(dialogOpen = joinVisitorDialogOpenState, R.string.join_visitor_success)
                if (joinDialogOpenState.value)
                    JoinCrewDialog(dialogOpen = joinDialogOpenState, R.string.join_success)
                if(notEnoughInfoDialogOpenState.value)
                    NotEnoughInfoDialog(dialogOpen = notEnoughInfoDialogOpenState)

                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (appbar, crewInfo, joinBtns) = createRefs()

                    SetAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(appbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            },
                        onClick = {
                            findNavController().navigateUp()
                        },
                        textString = R.string.crew
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(crewInfo) {
                                top.linkTo(appbar.bottom)
                                bottom.linkTo(joinBtns.top)
                                height = Dimension.fillToConstraints
                            }
                            .verticalScroll(scrollState)

                    ) {
                        CrewImageCard()
                        CrewInfo()
                        GrayDivider()

                        CrewIntroCard()
                        GrayDivider()

                        CrewExtraInfo()
                        GrayDivider()

                        CrewGuidance()
                    }

                    JoinCrewBtns(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .background(Color.White)
                            .constrainAs(joinBtns) {
                                bottom.linkTo(parent.bottom)
                            },

                        //todo: bottom dialog 구현 후 분기처리.
                        joinVisitorDialogOpenState = notEnoughInfoDialogOpenState, //joinVisitorDialogOpenState
                        joinDialogOpenState = joinDialogOpenState
                    )
                }
            }
        }
    }

    @Composable
    fun JoinCrewBtns(
        modifier: Modifier,
        joinVisitorDialogOpenState: MutableState<Boolean>,
        joinDialogOpenState: MutableState<Boolean>,
    ) {

        Column(
            modifier = modifier
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        colorResource(id = R.color.gray01)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {

                WhiteRoundBtn(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                    textString = R.string.join_as_visitor,
                    fontSize = 16.sp) {
                    joinVisitorDialogOpenState.value = true
                }

                Spacer(modifier = Modifier.width(12.dp))

                OrangeRoundBtn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    textString = R.string.join, fontSize = 16.sp
                ) {
                    joinDialogOpenState.value = true
                }
            }


        }

    }

    @Composable
    fun CrewGuidance() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp)
                .padding(horizontal = 25.dp)
        ) {

            Text(
                text = stringResource(id = R.string.guidance),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.main_black),
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 36.dp),
                text = "안내사항이 이 부분에 들어갑니다." + stringResource(id = R.string.medium_text),
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black)
            )
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
                color = colorResource(id = R.color.main_black)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(id = R.color.orange03))
                    .padding(top = 16.dp, bottom = 28.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.what_is_visitors),
                        color = colorResource(id = R.color.main_orange),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        text = stringResource(id = R.string.visitor_explanation),
                        color = colorResource(id = R.color.main_orange),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
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
                color = colorResource(id = R.color.main_black)
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
                color = colorResource(id = R.color.main_black)
            )
        }
    }

    @Composable
    fun CrewIntroKeyword() {

        //todo : 소개 키워드 받기. 
        val keywordList = listOf<String>(
//            "크루 키워드",
//            "이 부분에",
//            "들어갑니다.",
//            "초보 환영",
//            "코치님과 훈련",
//            "레슨 운영",
//            "훈련 중심",
//            "경기 중심"
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SportKeyword(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(colorResource(id = Sport.getNumOf(0).color))
                        .padding(horizontal = 13.dp, vertical = 4.dp),
                    sportName = Sport.getNumOf(0).sportName
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorResource(id = Sport.getNumOf(0).color))
                        .size(6.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "법정동",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }

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

    @Composable
    fun CrewImageCard() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp)
                .background(colorResource(id = Sport.getNumOf(0).color))
        ) {

            val (icon, pin) = createRefs()
            Image(painter = painterResource(id = Sport.getNumOf(0).icon),
                contentDescription = "crew sport icon",
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(76.dp))

            Image(painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = "crew detail pin",
                modifier = Modifier
                    .constrainAs(pin) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .size(24.dp))

        }
    }
}