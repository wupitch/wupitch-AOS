package wupitch.android.presentation.ui.main.impromptu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.CreateFab
import wupitch.android.presentation.ui.components.DistrictBottomSheetFragment
import wupitch.android.presentation.ui.main.impromptu.components.ImpromptuList

@AndroidEntryPoint
class ImpromptuFragment : Fragment() {

    private val viewModel: ImpromptuViewModel by viewModels()
    private lateinit var districtBottomSheet: DistrictBottomSheetFragment

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setContent {
                WupitchTheme {

                    //todo bottom sheet 열면 가져오게 하는게 나을듯??
                    val districtList = viewModel.district.observeAsState()
                    val impromptuList = viewModel.impromptuListState.value

                    districtList.value?.data?.let { list ->

                        districtBottomSheet = DistrictBottomSheetFragment(list, viewModel)

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {

                            val (toolbar, homeList, fab, progressbar) = createRefs()

                            ImpromptuToolbar(
                                modifier = Modifier.constrainAs(toolbar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                                districtOnClick = {
                                    districtBottomSheet.show(
                                        childFragmentManager,
                                        "district bottom sheet"
                                    )
                                })

                            impromptuList.data.let { list ->
                                ImpromptuList(
                                    modifier = Modifier.constrainAs(homeList) {
                                        top.linkTo(toolbar.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                        height = Dimension.fillToConstraints
                                    },
                                    list = list,
                                    navigationToCrewDetailScreen = {
                                        val bundle = Bundle().apply { putInt("crewId", it) }
                                        activity?.findNavController(R.id.main_nav_container_view)
                                            ?.navigate(
                                                R.id.action_mainFragment_to_impromptuDetailFragment, bundle)
                                    })
                                CreateFab(
                                    modifier = Modifier
                                        .constrainAs(fab) {
                                            end.linkTo(parent.end, margin = 24.dp)
                                            bottom.linkTo(parent.bottom, margin = 20.dp)
                                        },
                                    onClick = {
//                                        activity?.findNavController(R.id.main_nav_container_view)
//                                            ?.navigate(R.id.action_mainFragment_to_createCrewSport)
                                    })
                            }


                            if(impromptuList.isLoading){
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
    }

    @Composable
    fun ImpromptuToolbar(
        modifier: Modifier,
        districtOnClick: () -> Unit
    ) {
        ConstraintLayout(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (icon_down, icon_search, icon_filter, icon_notification, text) = createRefs()

                    Text(
                        text = stringResource(id = R.string.district),
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        color = colorResource(id = R.color.black),
                        fontSize = 22.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        modifier = Modifier
                            .constrainAs(icon_down) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(text.end, margin = 8.dp)
                            }
                            .size(24.dp),
                        onClick = { districtOnClick() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_down),
                            contentDescription = "district_filter"
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .constrainAs(icon_search) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(icon_filter.start, margin = 16.dp)
                            }
                            .size(24.dp),
                        onClick = {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_searchFragment)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "search"
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .constrainAs(icon_filter) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(icon_notification.start, margin = 16.dp)
                            }
                            .size(24.dp),
                        onClick = {
//                            activity?.findNavController(R.id.main_nav_container_view)
//                                ?.navigate(R.id.action_mainFragment_to_filterFragment)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "search"
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .constrainAs(icon_notification) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .size(24.dp),
                        onClick = {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "search"
                        )
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDistricts()
        viewModel.getImpromptuList()
    }
}
