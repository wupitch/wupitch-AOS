package wupitch.android.presentation.ui.main.my_activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.home.crew_detail.components.JoinSuccessDialog
import wupitch.android.presentation.ui.main.my_activity.components.*

@ExperimentalPagerApi
@AndroidEntryPoint
class MyCrewDetailFragment : Fragment() {

    private val viewModel: MyActivityViewModel by viewModels()

    private var selectedTab: Int = 0
    private val tabs = listOf<TabItem>(
        TabItem.CrewIntro,
        TabItem.CrewBoard,
        TabItem.CrewGallery,
        TabItem.CrewMembers
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getParcelable<MyCrewArg>("my_crew_info")?.let { crewInfo ->
            //todo : get crew from viewModel with the crewInfo.crewId
            selectedTab = crewInfo.selectedTab
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
                    val reportDialogOpenState = remember { viewModel.crewReportState }

                    if (reportDialogOpenState.value)
                        ReportDialog(dialogOpen = reportDialogOpenState, viewModel)

                    val scope = rememberCoroutineScope()
                    val pagerState = rememberPagerState(selectedTab)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        FullToolBar(
                            modifier = Modifier.fillMaxWidth(),
                            icon = R.drawable.more,
                            onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { showReportBottomSheet() },
                            textString = R.string.crew
                        )

                        MyCrewPager(
                            modifier = Modifier
                                .fillMaxSize(),
                            scope = scope,
                            pagerState = pagerState
                        )
                    }
                }
            }
        }
    }

    private fun showReportBottomSheet() {
        val reportBottomSheet = ReportBottomSheetFragment(viewModel)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }

    @Composable
    fun MyCrewPager(
        modifier: Modifier,
        scope: CoroutineScope,
        pagerState: PagerState
    ) {

        Scaffold(
            modifier = modifier,
            content = {
                TabPage(pagerState = pagerState, tabItems = tabs)
            },
            topBar = {
                MyCrewTabRow(
                    tabs = tabs,
                    pagerState = pagerState,
                    onPageSelected = { tabItem ->
                        scope.launch {
                            pagerState.scrollToPage(tabItem.index)
                        }
                    }
                )
            }
        )
    }

    @Composable
    fun MyCrewTabRow(
        tabs: List<TabItem>,
        pagerState: PagerState,
        onPageSelected: ((tabItem: TabItem) -> Unit),
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage,
            divider = {
                Divider(
                    Modifier
                        .height(0.dp)
                        .background(Color.Transparent)
                )
            },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(
                    modifier = Modifier
                        .background(Color.White)
                        .height(43.dp),
                    selected = index == pagerState.currentPage,
                    onClick = {
                        onPageSelected(tabItem)
                    },
                    text = {
                        Text(
                            text = stringResource(id = tabItem.title),
                            color = Color.Black,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun TabPage(pagerState: PagerState, tabItems: List<TabItem>) {
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
        ) { index ->
            tabItems[index].screenToLoad()
        }
    }
}

sealed class TabItem(
    val index: Int,
    @StringRes val title: Int,
    val screenToLoad: @Composable () -> Unit
) {
    object CrewIntro : TabItem(0, R.string.intro, {
        MyCrewIntroTab()
    })

    object CrewBoard : TabItem(1, R.string.board, {
        MyCrewBoardTab()
    })

    object CrewGallery : TabItem(2, R.string.gallery, {
        MyCrewGalleryTab()
    })

    object CrewMembers : TabItem(3, R.string.members, {
        MyCrewMembersTab()
    })
}