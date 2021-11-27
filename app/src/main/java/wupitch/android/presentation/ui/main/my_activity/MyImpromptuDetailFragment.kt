package wupitch.android.presentation.ui.main.my_activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.crew_detail.components.JoinSuccessDialog
import wupitch.android.presentation.ui.main.home.crew_detail.components.NotEnoughInfoDialog
import wupitch.android.presentation.ui.main.impromptu.ImpromptuViewModel
import wupitch.android.presentation.ui.main.impromptu.components.RemainingDays
import wupitch.android.presentation.ui.main.impromptu.impromptu_detail.ImprtDetailViewModel

@AndroidEntryPoint
class MyImpromptuDetailFragment : Fragment() {

    private val viewModel : ImprtDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("impromptu_id")?.let { id ->
            Log.d("{CrewDetailFragment.onCreate}", id.toString())
            viewModel.getImprtDetail(id)
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
                    val joinSuccessDialogOpenState = remember { mutableStateOf(false) }
                    val notEnoughInfoDialogOpenState = remember { mutableStateOf(false) }

                    val pinToggleState = remember { mutableStateOf(false)}

                    val joinState = viewModel.joinImpromptuState.value

                    if(joinState.isSuccess == true){
                        joinSuccessDialogOpenState.value = true
                        viewModel.initJoinImpromptuState()
                    }else if(joinState.isSuccess == false){
                        notEnoughInfoDialogOpenState.value = true
                        viewModel.initJoinImpromptuState()
                    }

                    if(joinState.error.isNotEmpty()){
                        Toast.makeText(requireContext(), viewModel.joinImpromptuState.value.error, Toast.LENGTH_SHORT).show()
                    }

                    if (joinSuccessDialogOpenState.value){
                        JoinSuccessDialog(
                            dialogOpen = joinSuccessDialogOpenState,
                            titleString = R.string.join_success_impromptu,
                            isImpromptu = true
                        )
                    }

                    if (notEnoughInfoDialogOpenState.value)
                        NotEnoughInfoDialog(
                            dialogOpen = notEnoughInfoDialogOpenState,
                            subtitleString = stringResource(id = R.string.not_enough_info_impromptu),
                        ){
                            //todo to profile edit screen?
                        }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, topDivider, infoContent, joinButton, bottomDivider, progressbar) = createRefs()

                        TitleToolbar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(toolbar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                            onLeftIconClick = { findNavController().navigateUp() },
                            textString = R.string.impromptu
                        )

                        if (scrollState.value > 202.dpToInt()) {
                            Divider(Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(infoContent.top)
                                }
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(infoContent) {
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(bottomDivider.top)
                                    height = Dimension.fillToConstraints
                                }
                                .verticalScroll(scrollState)

                        ) {
                            CrewImageCard(pinToggleState)

                            ImpromptuInfo()
                            GrayDivider()

                            ImpromptuIntroCard()
                            GrayDivider()

                            ImpromptuExtraInfo()
                            GrayDivider()

                            ImpromptuGuidance()
                        }

                        Divider(Modifier
                            .constrainAs(bottomDivider) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(joinButton.top, margin = 11.dp)
                                width = Dimension.fillToConstraints
                            }
                            .height(1.dp)
                            .background(colorResource(id = R.color.gray01)))

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(joinButton) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    bottom.linkTo(parent.bottom, margin = 12.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .height(44.dp),
                            btnColor = R.color.main_orange,
                            textString = R.string.join_impromptu,
                            fontSize = 16.sp
                        ){
                            viewModel.joinImpromptu()
                        }

                        if(joinState.isLoading){
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(bottomDivider.top)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun Int.dpToInt() = (this * requireContext().resources.displayMetrics.density).toInt()


    @Composable
    fun ImpromptuExtraInfo() {
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
                    .padding(top = 12.dp),
                text = "준비물이 이 부분에 들어갑니다." + stringResource(id = R.string.medium_text),
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
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
    fun ImpromptuIntroCard() {
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
            Spacer(modifier = Modifier.height(24.dp))

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
    fun ImpromptuInfo() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )
        ) {
            RemainingDays(
                modifier = Modifier,
                remainingDays = 1,
                isDetail = true
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "번개 이름이 이곳에 들어갑니다.",
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

                    modifier = Modifier.size(24.dp),
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
                    .padding(top = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
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
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
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
                    color = colorResource(id = R.color.main_black),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.people),
                    contentDescription = "won icon"
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    text = "2/3명 참여",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
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
            val (image, pin) = createRefs()

            Image(painter = painterResource(id = R.drawable.img_bungae_thumb),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .size(76.dp))

            PinToggleButton(modifier = Modifier
                .constrainAs(pin) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                } , toggleState = pinToggleState )


        }
    }
}