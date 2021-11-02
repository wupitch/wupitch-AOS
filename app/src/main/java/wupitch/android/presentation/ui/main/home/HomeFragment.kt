package wupitch.android.presentation.ui.main.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.data.remote.CrewCardInfo
import wupitch.android.presentation.theme.BottomSheetShape
import wupitch.android.presentation.ui.main.home.components.CrewCard
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.NumberPicker
import wupitch.android.presentation.ui.main.home.components.CrewList
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val districtList = arrayOf<String>(
        "서울시", "도봉구", "노원구", "강북구", "성북구", "은평구", "종로구", "동대문구",
        "중랑구", "서대문구", "중구", "성동구", "광진구", "마포구", "용산구", "강서구",
        "양천구", "구로구", "영등포구", "동작구", "관악구", "금천구", "서초구", "강남구",
        "송파구", "강동구"
    )

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                
                val districtValue = remember {
                    mutableStateOf(0)
                }


                    DistrictBottomSheet(districtValue)






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

    @ExperimentalMaterialApi
    @Composable
    fun DistrictBottomSheet(
        districtValueState : MutableState<Int>
    ) {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        BottomSheetScaffold(
            sheetShape = BottomSheetShape,
            scaffoldState = bottomSheetScaffoldState,
            sheetGesturesEnabled  = true,
            sheetContent = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(398.dp)
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        val (title, close, numPicker, btn_select) = createRefs()

                        Text(
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            },
                            text = stringResource(id = R.string.select_region_bottom_sheet),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.main_black)
                        )

                        Image(
                            modifier = Modifier
                                .constrainAs(close) {
                                    top.linkTo(title.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(title.bottom)
                                }
                                .size(24.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                },
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = "close icon"
                        )

                        AndroidView(modifier = Modifier
                            .width(108.dp)
                            .constrainAs(numPicker) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(title.bottom)
                                bottom.linkTo(btn_select.top)
                            },
                            factory = { context ->

                                android.widget.NumberPicker(context).apply {
                                    minValue = 0
                                    maxValue = districtList.size - 1
                                    displayedValues = districtList
                                    setOnValueChangedListener { picker, oldVal, newVal -> 
                                        districtValueState.value = newVal
                                    }
                                }
                            }
                        )

                        Button(
                            modifier = Modifier
                                .constrainAs(btn_select) {
                                    bottom.linkTo(parent.bottom, margin = 32.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            onClick = {
                            Log.d("{HomeFragment.DistrictBottomSheet}", districtValueState.value.toString())
                            // todo : viewModel 에 보내기.
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_black))) {

                            Text(text = stringResource(id = R.string.selection),
                            color = Color.White,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center)

                        }


                    }


                }
            },
            sheetPeekHeight = 0.dp
        ) {
            val crewList = viewModel.crewList.value
            val loading = viewModel.loading.value

            Column(modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.black03))) {
                HomeAppBar(districtOnClick = {
                    coroutineScope.launch {

                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }

                    }
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
