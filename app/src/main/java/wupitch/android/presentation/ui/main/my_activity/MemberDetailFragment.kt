package wupitch.android.presentation.ui.main.my_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.FullToolBar
import wupitch.android.presentation.ui.components.ReportBottomSheetFragment
import wupitch.android.util.SportType

@AndroidEntryPoint
class MemberDetailFragment : Fragment() {

    private val viewModel: MemberDetailViewModel by viewModels()
    private lateinit var reportBottomSheet: ReportBottomSheetFragment
    private lateinit var reportDialog: ReportDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("memberId")?.let {
            viewModel.getMemberInfo(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportDialog = ReportDialog(requireContext(), viewModel)
        viewModel.showReportDialog.observe(viewLifecycleOwner, Observer {
            if (it) reportDialog.show()
            else reportDialog.dismiss()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val memberState = remember { viewModel.memberInfoState }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, memberInfo, progressbar) = createRefs()

                        FullToolBar(
                            modifier = Modifier.constrainAs(toolbar) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                width = Dimension.fillToConstraints
                            },
                            onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { showReportBottomSheet() },
                            textString = R.string.profile,
                            icon = R.drawable.more
                        )
                        memberState.value.data?.let { memberData ->
                            MemberInfoLayout(
                                modifier = Modifier.constrainAs(memberInfo) {
                                    top.linkTo(toolbar.bottom, margin = 20.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                                member = memberData
                            )
                        }



                        if (memberState.value.isLoading) {
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
        reportBottomSheet = ReportBottomSheetFragment(viewModel)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }

    @Composable
    fun MemberInfoLayout(
        modifier: Modifier,
        member: MemberDetail
    ) {
        ConstraintLayout(modifier.fillMaxWidth()) {
            val (image, nickname, info, sports, intro) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start, margin = 20.dp)
                        top.linkTo(parent.top)
                    }
                    .size(64.dp)
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
                modifier = Modifier.constrainAs(nickname) {
                    start.linkTo(image.end, margin = 16.dp)
                    top.linkTo(parent.top, margin = 8.dp)
                },
                text = member.userName,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(id = R.color.main_black)
            )

            Row(modifier = Modifier.constrainAs(info) {
                top.linkTo(nickname.bottom, margin = 2.dp)
                start.linkTo(nickname.start)
            }, verticalAlignment = Alignment.CenterVertically) {
                InfoText(textString = member.userAgeGroup)
                InfoDotText()
                InfoText(textString = member.userArea)
                InfoDotText()
                InfoText(textString = member.userPhoneNum)
            }

            FlowRow(
                modifier = Modifier
                    .constrainAs(sports) {
                        start.linkTo(parent.start, margin = 24.dp)
                        top.linkTo(image.bottom, margin = 20.dp)
                    }
                    .fillMaxWidth()
                    .padding(end = 80.dp),
                mainAxisSpacing = 10.dp,
                crossAxisSpacing = 10.dp
            ) {
                member.userSports.forEach {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(id = SportType.getNumOf(it.sportsId).color))
                            .padding(horizontal = 11.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = it.name,
                            color = Color.White,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )
                    }
                }
            }

            Box(modifier = Modifier
                .constrainAs(intro) {
                    start.linkTo(parent.start)
                    top.linkTo(sports.bottom, margin = 32.dp)
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(164.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = R.color.gray04))
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .padding(top = 11.dp),
                    text = member.intro,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.main_black)
                )
            }
        }
    }

    @Composable
    fun InfoText(
        textString: String
    ) {
        Text(
            text = textString,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = colorResource(id = R.color.gray02)
        )
    }

    @Composable
    fun InfoDotText() {
        Text(
            text = stringResource(id = R.string.impromptu_guide_dot),
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = colorResource(id = R.color.gray02)
        )
    }

}