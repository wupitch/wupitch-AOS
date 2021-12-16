package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.FullToolBar
import wupitch.android.presentation.ui.components.MemberBottomSheetFragment
import wupitch.android.presentation.ui.components.ReportBottomSheetFragment
import wupitch.android.presentation.ui.main.my_activity.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.components.MemberInfoLayout
import wupitch.android.util.ReportType
import java.lang.reflect.Member

@AndroidEntryPoint
class ImprtMemberDetailFragment : Fragment() {

    private val viewModel: ImprtMemberDetailViewModel by viewModels()
    private lateinit var memberBottomSheet: MemberBottomSheetFragment
    private lateinit var reportBottomSheet : ReportBottomSheetFragment
    private lateinit var reportDialog: ReportDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            getInt("memberId").let {
                viewModel.memberId = it
            }
            getInt("imprtId").let {
                viewModel.imprtId = it
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

                    val dismissMemberState = viewModel.dismissMember
                    if(dismissMemberState.value.isSuccess) {
                        Toast.makeText(requireContext(), "멤버 삭제에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if(dismissMemberState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(), dismissMemberState.value.error, Toast.LENGTH_SHORT).show()
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
                                //todo 현재 유저가 리더인지 확인.
                                showMemberBottomSheet()
                                //리더가 아니라면 신고하기 바텀시트.
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

    private fun showReportBottomSheet() {
        reportBottomSheet = ReportBottomSheetFragment(viewModel, ReportType.CREW)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }



}