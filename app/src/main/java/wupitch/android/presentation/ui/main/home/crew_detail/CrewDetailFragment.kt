package wupitch.android.presentation.ui.main.home.crew_detail

import android.os.Bundle
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
import androidx.compose.runtime.*
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.crew_detail.components.JoinSuccessDialog
import wupitch.android.presentation.ui.main.home.crew_detail.components.NotEnoughInfoDialog
import wupitch.android.util.Sport

@AndroidEntryPoint
class CrewDetailFragment : Fragment() {

    private val viewModel: CrewDetailViewModel by viewModels()
    private lateinit var visitorBottomSheet: VisitorBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("crewId")?.let { id ->
            viewModel.getCrewDetail(id)
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
                    val crewState = remember { viewModel.crewDetailState }
                    if (crewState.value.error.isNotEmpty()) {
                        Toast.makeText(requireContext(), crewState.value.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    val scrollState = rememberScrollState(0)

                    val joinVisitorDialogOpenState = remember { mutableStateOf(false) }
                    val joinDialogOpenState = remember { mutableStateOf(false) }
                    val notEnoughInfoDialogOpenState = remember { mutableStateOf(false) }

                    val isPinnedState = remember { mutableStateOf(false) } //todo init value : crewState.value.isPinned
                    if(viewModel.pinState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), viewModel.pinState.value.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    val joinState = remember {viewModel.joinState } //todo remember?
                    if(joinState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), viewModel.pinState.value.error, Toast.LENGTH_SHORT)
                            .show()
                    }
                    if(joinState.value.isSuccess){
                        joinDialogOpenState.value = true
                    }else {
                        when(joinState.value.code){
                            //todo fix code
                            0 -> {
                                notEnoughInfoDialogOpenState.value = true
                            }
                        }
                    }


                    if (joinVisitorDialogOpenState.value)
                        JoinSuccessDialog(
                            dialogOpen = joinVisitorDialogOpenState,
                            R.string.join_visitor_success
                        ){
                            joinVisitorDialogOpenState.value  = false
                        }
                    if (joinDialogOpenState.value)
                        JoinSuccessDialog(dialogOpen = joinDialogOpenState, R.string.join_success){
                            joinDialogOpenState.value = false
                            viewModel.initJoinState()
                        }
                    if (notEnoughInfoDialogOpenState.value)
                        NotEnoughInfoDialog(
                            dialogOpen = notEnoughInfoDialogOpenState,
                            subtitleString = stringResource(id = R.string.not_enough_info_subtitle)
                        ) {
                            viewModel.setNotEnoughInfo()
                            val bundle = Bundle().apply { putInt("tabId", R.id.myPageFragment) }
                            findNavController().navigate(R.id.action_crewDetailFragment_to_mainFragment, bundle)
                        }


                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, divider, crewInfoCol, joinBtns, progressbar) = createRefs()

                        TitleToolbar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(toolbar) {
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
                                        top.linkTo(toolbar.bottom)
                                        bottom.linkTo(crewInfoCol.top)
                                    }
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(colorResource(id = R.color.gray01))
                            )
                        }
                        crewState.value.data?.let { crewInfo ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(crewInfoCol) {
                                        top.linkTo(toolbar.bottom)
                                        bottom.linkTo(joinBtns.top)
                                        height = Dimension.fillToConstraints
                                    }
                                    .verticalScroll(scrollState)

                            ) {
                                CrewImageCard(isPinnedState, crewInfo){
                                    isPinnedState.value = it
                                    viewModel.changePinStatus()
                                }
                                CrewInfo(crewInfo)
                                GrayDivider()

                                CrewIntroCard(crewInfo)
                                GrayDivider()

                                CrewExtraInfo(crewInfo)
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
                                crewState = crewInfo,
                            )
                        }

                        if (crewState.value.isLoading|| joinState.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(parent.bottom)
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
    fun JoinCrewBtns(
        modifier: Modifier,
        crewState: CrewDetailResult
    ) {

        Column(
            modifier = modifier
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(id = R.color.gray01))
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
                        crewState.guestDues, crewState.visitDays
                    )
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
                    viewModel.participateCrew()
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
                    text = crewState.ageTable,
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
                    .padding(top = 2.dp),
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

    @Composable
    fun CrewImageCard(
        pinToggleState: MutableState<Boolean>,
        crewState: CrewDetailResult,
        onValueChange : (Boolean) -> Unit
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp)
        ) {

            val (icon, pin) = createRefs()

            Image(painter = if (crewState.crewImage != null) {
                rememberImagePainter(
                    crewState.crewImage,
                    builder = {
                        placeholder(Sport.getNumOf(crewState.sportsId).detailImage)
                        build()
                    }
                )
            } else {
                rememberImagePainter(Sport.getNumOf(crewState.sportsId).detailImage)
            },
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


            //todo
            PinToggleButton(modifier = Modifier
                .constrainAs(pin) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }, toggleState = pinToggleState,
                onValueChange = onValueChange
            )

        }
    }
}