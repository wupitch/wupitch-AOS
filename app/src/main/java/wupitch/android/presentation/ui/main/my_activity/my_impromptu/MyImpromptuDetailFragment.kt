package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import android.os.Bundle
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.ImprtDetailResult
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.impromptu.components.RemainingDays
import wupitch.android.presentation.ui.main.my_activity.components.ReportDialog

@AndroidEntryPoint
class MyImpromptuDetailFragment : Fragment() {

    private val viewModel : MyImpromptuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("impromptuId")?.let { id ->
            viewModel.getImprtDetail(id)
        }
    }

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val reportDialogOpenState = remember { viewModel.crewReportState }

                    if (reportDialogOpenState.value)
                        ReportDialog(dialogOpen = reportDialogOpenState, viewModel)

                    val imprtState = remember { viewModel.imprtDetailState }
                    if (imprtState.value.error.isNotEmpty()) {
                        Toast.makeText(requireContext(), imprtState.value.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    val scrollState = rememberScrollState(0)

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, topDivider, infoContent, progressbar) = createRefs()

                        FullToolBar(
                            modifier = Modifier .fillMaxWidth()
                                .constrainAs(toolbar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                            icon = R.drawable.more,
                            onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { showReportBottomSheet() },
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
                        imprtState.value.data?.let { imprtInfo ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(infoContent) {
                                        top.linkTo(toolbar.bottom)
                                        bottom.linkTo(parent.bottom)
                                        height = Dimension.fillToConstraints
                                    }
                                    .verticalScroll(scrollState)

                            ) {
                                ImprtImageCard(imprtInfo)

                                ImpromptuInfo(imprtInfo)
                                GrayDivider()

                                ImpromptuIntroCard(imprtInfo)
                                GrayDivider()

                                ImpromptuExtraInfo(imprtInfo)
                                GrayDivider()

                                ImpromptuGuidance()
                            }
                        }

                        if(imprtState.value.isLoading){
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

    private fun showReportBottomSheet() {
        val reportBottomSheet = ReportBottomSheetFragment(viewModel)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }

    private fun Int.dpToInt() = (this * requireContext().resources.displayMetrics.density).toInt()


    @Composable
    fun ImpromptuExtraInfo(
        imprtInfo: ImprtDetailResult
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp, bottom = 24.dp)
                .padding(horizontal = 25.dp)
        ) {

             if (imprtInfo.materials != null) {
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
                    text = imprtInfo.materials,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.main_black),
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
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
                text = imprtInfo.inquiries,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 24.sp
            )
        }
    }

    @Composable
    fun ImpromptuIntroCard(
        imprtInfo: ImprtDetailResult
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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = imprtInfo.introduction,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.main_black),
                lineHeight = 24.sp
            )
        }
    }

    @Composable
    fun ImpromptuInfo(
        imprtInfo: ImprtDetailResult
    ) {
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
                remainingDays = imprtInfo.dday,
                isDetail = true
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = imprtInfo.title,
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
                        text = imprtInfo.date,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        color = colorResource
                            (id = R.color.main_black),
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = imprtInfo.time,
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
                    text = imprtInfo.location,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = colorResource
                        (id = R.color.main_black),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }

            if (imprtInfo.dues != null) {
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
                        text = imprtInfo.dues,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.main_black),
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
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
                    text = imprtInfo.recruitStatus,
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
    fun ImprtImageCard(
        imprtInfo: ImprtDetailResult
        ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp)
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(202.dp),
                painter = if (imprtInfo.impromptuImage != null) {
                    rememberImagePainter(
                        imprtInfo.impromptuImage,
                        builder = {
                            placeholder(R.drawable.img_bungae_thumb)
                            build()
                        }
                    )
                } else {
                    painterResource(id = R.drawable.img_bungae_thumb)
                },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}