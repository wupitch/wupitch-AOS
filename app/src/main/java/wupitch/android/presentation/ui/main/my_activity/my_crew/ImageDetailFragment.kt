package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import wupitch.android.R
import wupitch.android.presentation.theme.ImageDetailTheme
import wupitch.android.presentation.theme.Roboto

class ImageDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ImageDetailTheme {
                    val likeToggleState = remember { mutableStateOf(false) }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.main_black))
                    ) {

                        val (toolbar, userImage, userName, like, image) = createRefs()

                        LikeToolbar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, toggleState = likeToggleState, onBackClick = {
                            findNavController().navigateUp()
                        }) {
                            //todo send like toggle to server.

                        }
                        Image(
                            modifier = Modifier
                                .constrainAs(image) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    top.linkTo(toolbar.bottom)
//                                    height = Dimension.wrapContent
                                    width = Dimension.fillToConstraints
                                },
                            painter = rememberImagePainter(
                                "https://cdn.clien.net/web/api/file/F01/9581533/fb803eda1eed7.jpg?w=780&h=30000", //9:16
                                builder = {
                                    placeholder(R.color.main_black)
                                    build()
                                }
                            ), contentDescription = null,
                            contentScale = ContentScale.FillWidth
                        )

                        Image(
                            modifier = Modifier
                                .constrainAs(userImage) {
                                    start.linkTo(parent.start, margin = 20.dp)
                                    top.linkTo(toolbar.bottom, margin = 20.dp)
                                }.size(28.dp),
                            painter = rememberImagePainter(
                                "https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg",
                                builder = {
                                    placeholder(R.color.main_black)
                                    transformations(CircleCropTransformation())
                                    build()
                                }
                            ), contentDescription = null
                        )
                        Text(
                            modifier = Modifier.constrainAs(userName) {
                                start.linkTo(userImage.end, margin = 6.dp)
                                top.linkTo(userImage.top)
                                bottom.linkTo(userImage.bottom)
                            },
                            text = "유저 닉네임",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier.constrainAs(like) {
                                end.linkTo(parent.end, margin = 20.dp)
                                top.linkTo(userImage.top)
                                bottom.linkTo(userImage.bottom)
                            },
                            text = "20명이 좋아해요",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun LikeToolbar(
        modifier: Modifier,
        toggleState: MutableState<Boolean>,
        onBackClick: () -> Unit,
        onLikeClick: () -> Unit
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.main_black))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            val (back, like) = createRefs()

            Icon(modifier = Modifier
                .constrainAs(back) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(24.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null)
                { onBackClick() },
                painter = painterResource(id = R.drawable.left),
                contentDescription = "go back previous page",
                tint = Color.White
            )

            Image(
                modifier = Modifier
                    .constrainAs(like) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .size(24.dp)
                    .toggleable(
                        value = toggleState.value,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = true,
                        role = Role.Checkbox,
                        onValueChange = {
                            toggleState.value = it
                            onLikeClick() //todo pass id.
                        }
                    ),
                painter = if (toggleState.value) painterResource(id = R.drawable.like_filled) else painterResource(
                    id = R.drawable.like
                ),
                contentDescription = null
            )
        }
    }
}