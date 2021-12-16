package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.CreateFab
import wupitch.android.presentation.ui.components.ReportBottomSheetFragment
import wupitch.android.presentation.ui.main.home.components.CrewCard
import wupitch.android.presentation.ui.main.my_activity.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.components.MyCrewPostCard
import wupitch.android.util.ReportType

@AndroidEntryPoint
class MyCrewBoardFragment : Fragment() {

    private val viewModel: MyCrewViewModel by viewModels({ requireParentFragment() })
    private lateinit var reportBottomSheet : ReportBottomSheetFragment
    private lateinit var reportDialog : ReportDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCrewPosts()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val postState = remember { viewModel.crewPostState }
                    if(postState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(),postState.value.error , Toast.LENGTH_SHORT).show()
                    }

                    val likeState = remember { viewModel.postLikeState }
                    if(likeState.value.error.isNotEmpty()){
                        Toast.makeText(requireContext(),likeState.value.error , Toast.LENGTH_SHORT).show()
                    }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (postList, chrt, text, fab, progressbar) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

                        if (postState.value.data.isNotEmpty()) {

                            Box(modifier = Modifier
                                .constrainAs(postList) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                }
                                .background(Color.White)) {

                                LazyColumn {
                                    itemsIndexed(
                                        items = postState.value.data
                                    ) { index, item ->
                                        MyCrewPostCard(
                                            post = item,
                                            onMoreClick = { postId ->
                                               showReportBottomSheet(postId)
                                            },
                                            onLikeClick = { postId ->
                                                viewModel.patchPostLike(postId)
                                            })
                                    }
                                }
                            }
                        } else {
                            if (!postState.value.isLoading) {
                                Image(
                                    modifier = Modifier
                                        .constrainAs(chrt) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(text.top, margin = 24.dp)
                                        }
                                        .width(130.dp)
                                        .height(210.dp),
                                    painter = painterResource(id = R.drawable.img_chrt_02),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.constrainAs(text) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(guildLine)
                                    },
                                    text = "게시물이 없습니다.",
                                    color = colorResource(id = R.color.gray02),
                                    fontFamily = Roboto,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }

                        if (postState.value.isLoading) {
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
                        CreateFab(
                            modifier = Modifier
                                .constrainAs(fab) {
                                    end.linkTo(parent.end, margin = 24.dp)
                                    bottom.linkTo(parent.bottom, margin = 20.dp)
                                },
                            onClick = {
                                viewModel.selectedTab = 1
                                val bundle =
                                    Bundle().apply { putInt("crewId", viewModel.crewId) }
                                findNavController().navigate(
                                    R.id.action_myCrewDetailFragment_to_createMyCrewPostFragment,
                                    bundle
                                )
                            })

                    }
                }
            }
        }
    }

    private fun showReportBottomSheet(postId : Int) {
        reportBottomSheet = ReportBottomSheetFragment(viewModel, ReportType.POST, postId)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }
}