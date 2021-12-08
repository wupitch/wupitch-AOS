package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.CreateFab
import wupitch.android.presentation.ui.components.GallerySelect
import wupitch.android.presentation.ui.main.home.components.CrewCard
import wupitch.android.presentation.ui.main.my_activity.components.GalleryImage
import wupitch.android.presentation.ui.main.my_activity.components.ImageShareDialog

@AndroidEntryPoint
class MyCrewGalleryFragment : Fragment() {
    private val viewModel: MyCrewViewModel by viewModels({ requireParentFragment() })
    private val dataList = listOf<String>(
//        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
//        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
    )

    @ExperimentalFoundationApi
    @ExperimentalPermissionsApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val imageUri = remember { mutableStateOf<Uri>(Constants.EMPTY_IMAGE_URI) }
                    val openGallery = remember { mutableStateOf(false) }

                    val imageShareDialog = remember { mutableStateOf(false) }
                    if (imageShareDialog.value) {
                        ImageShareDialog(
                            dialogOpen = imageShareDialog,
                            onShareClick = { shareOnlyToCrew ->
                                viewModel.selectedTab = 2
                                if (shareOnlyToCrew) {
                                    Log.d("{MyCrewGalleryFragment.onCreateView}", "크루에만 올리기")
                                    viewModel.shareOnlyToCrew = true
                                } else {
                                    Log.d("{MyCrewGalleryFragment.onCreateView}", "피드에도 올리기")
                                    viewModel.shareOnlyToCrew = false
                                }
                                openGallery.value = true
                            }
                        )
                    }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(start = 6.dp)
                    ) {
                        val (chrt, text, fab) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

//                        LazyVerticalGrid(
//                            cells = GridCells.Fixed(3)
//                        ) {
//                            itemsIndexed(
//                                items = dataList
//                            ) { index, item ->
////                                viewModel.onChangeScrollPosition(index)
////                                if((index +1) >= (page * Constants.PAGE_SIZE) && !loading.value){
////                                    viewModel.getNewPage()
////                                }
////                                CrewCard(crewCard = crew) {
////                                    val bundle = Bundle().apply { putInt("crewId", crew.id) }
////                                    activity?.findNavController(R.id.main_nav_container_view)
////                                        ?.navigate(
////                                            R.id.action_mainFragment_to_crewDetailFragment,
////                                            bundle
////                                        )
////                                }
//                                GalleryImage(item, true) {         //todo
//                                    viewModel.selectedTab = 2
//                                    findNavController().navigate(R.id.action_myCrewDetailFragment_to_imageDetailFragment)
//                                }
//                            }
//                        }

                        if(dataList.isEmpty()){
                            Image(
                                modifier = Modifier
                                    .constrainAs(chrt) {
                                        bottom.linkTo(text.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .size(130.dp, 210.dp),
                                painter = painterResource(id = R.drawable.ic_img_chrt_03),
                                contentDescription = null)

                            Text(
                                modifier = Modifier
                                    .constrainAs(text) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(guildLine)
                                    }
                                    .padding(top = 24.dp),
                                text = stringResource(R.string.no_crew_image),
                                color = colorResource(id = R.color.gray02),
                                fontFamily = Roboto,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
//                        Image(
//                            modifier = Modifier
//                                .constrainAs(chrt) {
//                                    bottom.linkTo(text.top)
//                                    start.linkTo(parent.start)
//                                    end.linkTo(parent.end)
//                                }
//                                .size(130.dp, 210.dp),
//                            painter = painterResource(id = R.drawable.img_chrt_02),
//                            contentDescription = null)
//
//                        Text(
//                            modifier = Modifier
//                                .constrainAs(text) {
//                                    start.linkTo(parent.start)
//                                    end.linkTo(parent.end)
//                                    bottom.linkTo(guildLine)
//                                }
//                                .padding(top = 24.dp),
//                            text = stringResource(R.string.preparing),
//                            color = colorResource(id = R.color.gray02),
//                            fontFamily = Roboto,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Normal
//                        )

//                        CreateFab(
//                            modifier = Modifier
//                                .constrainAs(fab) {
//                                    end.linkTo(parent.end, margin = 24.dp)
//                                    bottom.linkTo(parent.bottom, margin = 20.dp)
//                                },
//                            onClick = {
//                                imageShareDialog.value = true
//                            })
//
//                        if (openGallery.value) {
//                            GallerySelect(
//                                onImageUri = { uri ->
//                                    if (uri != Constants.EMPTY_IMAGE_URI) {
//                                        imageUri.value = uri
//                                        viewModel.setUserImage(uri)
//                                    }
//                                    openGallery.value = false
//                                }
//                            )
//                        }

                    }

                }
            }
        }
    }


}