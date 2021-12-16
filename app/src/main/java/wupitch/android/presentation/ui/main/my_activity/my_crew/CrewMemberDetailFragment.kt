package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import wupitch.android.domain.model.MemberDetail
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.FullToolBar
import wupitch.android.presentation.ui.components.MemberBottomSheetFragment
import wupitch.android.presentation.ui.components.ReportBottomSheetFragment
import wupitch.android.presentation.ui.main.my_activity.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.components.MemberInfoLayout
import wupitch.android.util.ReportType
import wupitch.android.util.SportType

@AndroidEntryPoint
class CrewMemberDetailFragment : Fragment() {

    private val viewModel: CrewMemberDetailViewModel by viewModels()
    private lateinit var memberBottomSheet: MemberBottomSheetFragment
    private lateinit var reportDialog: ReportDialog
    private lateinit var visitorBottomSheetFragment: VisitorBottomSheetFragment
    private lateinit var memberToBeBottomSheetFragment: MemberToBeBottomSheetFragment
    private lateinit var reportBottomSheet: ReportBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            getInt("memberId").let {
                viewModel.memberId = it
            }
            getInt("crewId").let {
                viewModel.crewId = it
            }
        }
        viewModel.getMemberInfo()

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

                    val acceptState = viewModel.acceptState
                    if(acceptState.value.isSuccess) {
                        Toast.makeText(requireContext(), "수락에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if(acceptState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), acceptState.value.error, Toast.LENGTH_SHORT).show()
                    }

                    val declineState = viewModel.declineState
                    if(declineState.value.isSuccess) {
                        Toast.makeText(requireContext(), "거절에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if(declineState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), declineState.value.error, Toast.LENGTH_SHORT).show()
                    }

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
                            onRightIconClick = {
                                if(viewModel.isCurrentUserLeader){
                                    if(viewModel.memberStatus.isValid){
                                        showMemberBottomSheet()
                                    }else {
                                        if(viewModel.memberStatus.isGuest){
                                            showVisitorBottomSheet()
                                        }else {
                                            showMemberToBeBottomSheet()
                                        }
                                    }
                                }else {
                                   showReportBottomSheet()
                                }
                                               },
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

    private fun showMemberBottomSheet() {
        memberBottomSheet = MemberBottomSheetFragment(viewModel)
        memberBottomSheet.show(childFragmentManager, "member bottom sheet")
    }
    private fun showVisitorBottomSheet() {
        visitorBottomSheetFragment = VisitorBottomSheetFragment(viewModel)
        visitorBottomSheetFragment.show(childFragmentManager, "member bottom sheet")
    }
    private fun showMemberToBeBottomSheet() {
        memberToBeBottomSheetFragment = MemberToBeBottomSheetFragment(viewModel)
        memberToBeBottomSheetFragment.show(childFragmentManager, "member bottom sheet")
    }
    private fun showReportBottomSheet() {
        reportBottomSheet = ReportBottomSheetFragment(viewModel, ReportType.MEMBER)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }


}