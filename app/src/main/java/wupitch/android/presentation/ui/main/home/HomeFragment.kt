package wupitch.android.presentation.ui.main.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.main.home.components.CrewList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
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
                    districtBottomSheet = DistrictBottomSheetFragment()

                    //todo : viewmodel 을 bottom sheet constructor 로 inject 하기??
                    val crewList = viewModel.crewList.value
                    val loading = viewModel.loading.value

                    Column(
                        modifier = Modifier
                            .fillMaxSize().background(Color.White)
                    ) {
                        HomeAppBar(districtOnClick = {
                            districtBottomSheet.show(childFragmentManager, "district bottom sheet")
                        })
                        CrewList(
                            loading = loading,
                            crewList = crewList,
                            navigationToCrewDetailScreen = {
                                val bundle = Bundle().apply { putInt("crewId", it) }
                                activity?.findNavController(R.id.main_nav_container_view)?.navigate(
                                    R.id.action_mainFragment_to_crewDetailFragment, bundle
                                )
                            })
                    }
                }
            }
        }
    }

    @Composable
    fun HomeAppBar(
        districtOnClick: () -> Unit
    ) {
        ConstraintLayout {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
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
                        onClick = { Log.d("{HomeFragment.MainAppBar}", "on click search!") }
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
                        onClick = { Log.d("{HomeFragment.MainAppBar}", "on click filter!") }
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
                        onClick = { Log.d("{HomeFragment.MainAppBar}", "on click noti!") }
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
}
