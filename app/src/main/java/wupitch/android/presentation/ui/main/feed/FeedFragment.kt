package wupitch.android.presentation.ui.main.feed

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.CreateFab
import wupitch.android.presentation.ui.components.GallerySelect
import wupitch.android.presentation.ui.components.NotiToolbar
import wupitch.android.presentation.ui.main.my_activity.components.GalleryImage
import wupitch.android.presentation.ui.main.my_activity.components.ImageShareDialog

class FeedFragment : Fragment() {

    private val dataList = listOf<String>(
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
        "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
    )

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {


                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, images, chrt, text, fab) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

                        NotiToolbar(
                            modifier = Modifier
                                .constrainAs(toolbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                }
                                .fillMaxWidth(),
                            textString = stringResource(id = R.string.feed)
                        ) {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                        }

                        LazyVerticalGrid(
                            modifier = Modifier.constrainAs(images) {
                                top.linkTo(toolbar.bottom)
                                start.linkTo(parent.start, margin = 6.dp)
                                end.linkTo(parent.end)
                            },
                            cells = GridCells.Fixed(3)
                        ) {
                            itemsIndexed(
                                items = dataList
                            ) { index, item ->
//                                viewModel.onChangeScrollPosition(index)
//                                if((index +1) >= (page * Constants.PAGE_SIZE) && !loading.value){
//                                    viewModel.getNewPage()
//                                }
//                                CrewCard(crewCard = crew) {
//                                    val bundle = Bundle().apply { putInt("crewId", crew.id) }
//                                    activity?.findNavController(R.id.main_nav_container_view)
//                                        ?.navigate(
//                                            R.id.action_mainFragment_to_crewDetailFragment,
//                                            bundle
//                                        )
//                                }
                                GalleryImage(item, true) {
                                    //todo
                                }


                            }
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


                    }

                }
            }
        }
    }
}