package wupitch.android.presentation.ui.main.my_activity

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

class MyCrewDetailFragment : Fragment() {

    private lateinit var visitorBottomSheet: VisitorBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("crew_id")?.let { id ->
//            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
            //todo : get crew from viewModel with the id.
            Log.d("{CrewDetailFragment.onCreate}", id.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val scrollState = rememberScrollState(0)
                    val joinVisitorDialogOpenState = remember { mutableStateOf(false) }
                    val joinDialogOpenState = remember { mutableStateOf(false) }
                    val notEnoughInfoDialogOpenState = remember { mutableStateOf(false) }

                    val isPinnedState = remember{ mutableStateOf(false)}

                    if (joinVisitorDialogOpenState.value)
                        JoinSuccessDialog(
                            dialogOpen = joinVisitorDialogOpenState,
                            R.string.join_visitor_success
                        )
                    if (joinDialogOpenState.value)
                        JoinSuccessDialog(dialogOpen = joinDialogOpenState, R.string.join_success)
                    if (notEnoughInfoDialogOpenState.value)
                        NotEnoughInfoDialog(
                            dialogOpen = notEnoughInfoDialogOpenState,
                            subtitleString = stringResource(id = R.string.not_enough_info_subtitle)
                        ){
                            //todo to profile edit screen?
                        }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (appbar, divider, crewInfo, joinBtns) = createRefs()

                        TitleToolbar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(appbar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                            onLeftIconClick = {
                                findNavController().navigateUp()
                            },
                            textString = R.string.crew
                        )
                        if (scrollState.value > 202.dpToInt()) {
                            Divider(
                                Modifier
                                    .constrainAs(divider) {
                                        top.linkTo(appbar.bottom)
                                        bottom.linkTo(crewInfo.top)
                                    }
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(colorResource(id = R.color.gray01))
                            )
                        }


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
                            CrewImageCard(isPinnedState)
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

    }

    fun Int.dpToInt() = (this * requireContext().resources.displayMetrics.density).toInt()


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

                WhiteRoundBtn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    textString = stringResource(id = R.string.join_as_visitor),
                    fontSize = 16.sp,
                    textColor = R.color.main_orange,
                    borderColor = R.color.main_orange
                ) {
                    // todo 손님 신청 완료시. joinVisitorDialogOpenState.value = true
                    visitorBottomSheet = VisitorBottomSheetFragment(
                        "1만원",
                        listOf("21.00.00수", "21.00.00목", "21.00.00금")
                    ) //"21.00.00수", "21.00.00목","21.00.00금"
                    visitorBottomSheet.show(childFragmentManager, "visitor bottom sheet")
                }

                Spacer(modifier = Modifier.width(12.dp))

                RoundBtn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    btnColor = R.color.main_orange,
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
                .padding(top = 20.dp, bottom = 60.dp)
                .padding(horizontal = 25.dp)
        ) {

            Text(
                text = stringResource(id = R.string.guidance),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.main_black),
                fontSize = 16.sp
            )
            Row(Modifier.padding(top = 12.dp)) {
                Text(
                    text = stringResource(id = R.string.impromptu_guide_dot),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black)
                )
                Text(
                    text = stringResource(id = R.string.crew_guide1),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
                    lineHeight = 22.sp
                )
            }
            Row(Modifier.padding(top = 22.dp)) {
                Text(
                    text = stringResource(id = R.string.impromptu_guide_dot),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black)
                )
                Text(
                    text = stringResource(id = R.string.crew_guide2),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
                    lineHeight = 22.sp
                )
            }
            Row(Modifier.padding(top = 22.dp)) {
                Text(
                    text = stringResource(id = R.string.impromptu_guide_dot),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black)
                )
                Text(
                    text = stringResource(id = R.string.crew_guide3),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
                    lineHeight = 22.sp
                )
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

    @Composable
    fun CrewImageCard(
        pinToggleState : MutableState<Boolean>
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp)
        ) {

            val (icon, pin) = createRefs()

            Image(painter = painterResource(id = Sport.getNumOf(0).detailImage),
                contentDescription = "crew sport icon",
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .size(76.dp),
                contentScale = ContentScale.Crop
            )


            PinToggleButton(modifier = Modifier
                .constrainAs(pin) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                } , toggleState = pinToggleState )

        }
    }
}