package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.GrayDivider
import wupitch.android.presentation.ui.components.NotiToolbar
import wupitch.android.presentation.ui.main.my_page.components.FillInfoSnackbar

class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(key1 = snackbarHostState, block = {
                    snackbarHostState.showSnackbar(
                        message = getString(R.string.fill_info_guide),
                        duration = SnackbarDuration.Short
                    )
                })

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    NotiToolbar(
                        modifier = Modifier.fillMaxWidth(),
                        textString = stringResource(id = R.string.my_page)
                    ) {
                        activity?.findNavController(R.id.main_nav_container_view)
                            ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        val (profile, alert, myActivity, myRecord) = createRefs()

                        ProfileBox(
                            modifier = Modifier
                                .constrainAs(profile) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )

                        MyPageText(
                            modifier = Modifier
                                .constrainAs(myActivity) {
                                    start.linkTo(parent.start)
                                    top.linkTo(profile.bottom, margin = 22.dp)
                                },
                            textString = stringResource(id = R.string.interested_activity)
                        ){

                        }

                        MyPageText(
                            modifier = Modifier
                                .constrainAs(myRecord) {
                                    start.linkTo(parent.start)
                                    top.linkTo(myActivity.bottom)
                                },
                            textString = stringResource(id = R.string.my_record)
                        ){

                        }
                        FillInfoSnackbar(
                            modifier = Modifier.constrainAs(alert){
                                end.linkTo(parent.end)
                                top.linkTo(profile.top, margin = 93.dp)
                            },
                            snackbarHostState = snackbarHostState
                        )
                    }

                    GrayDivider()
                    Spacer(modifier = Modifier.height(4.dp))
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.announcement)
                    ){

                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.faq)
                    ){

                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.settings)
                    ){

                    }
                }
            }
        }

    }

    @Composable
    private fun MyPageText(
        modifier: Modifier,
        textString: String,
        onClick : () -> Unit
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    onClick()
                },
            text = textString,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
    }


    @Composable
    private fun ProfileBox(modifier: Modifier) {

        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.gray04))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    width = 2.dp,
                    color = colorResource(id = R.color.gray01)
                )
        ) {
            val (image, camera, text, button) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 18.dp)
                        bottom.linkTo(parent.bottom, margin = 18.dp)
                        start.linkTo(parent.start, margin = 24.dp)
                    }
                    .size(64.dp),
                painter = painterResource(id = R.drawable.profile_basic),
                contentDescription = null
            )
            
            IconButton(
                modifier = Modifier
                    .constrainAs(camera) {
                        top.linkTo(image.top, margin = 32.dp)
                        start.linkTo(image.start, margin = 32.dp)
                    },
                onClick = {

                }){
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.btn_photo), contentDescription = null )
                }

            Column(modifier = Modifier.constrainAs(text) {
                top.linkTo(image.top, margin = 8.dp)
                start.linkTo(image.end, margin = 20.dp)
                end.linkTo(button.start, margin = 10.dp)
                width = Dimension.fillToConstraints
            }) {
                Text(
                    text = "사용자 닉네임",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )

                Text(
                    modifier = Modifier.padding(top=2.dp),
                    text = "사용자 소개djksldjksldjklsjkfldsjkfldsjaklfdjka;",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.gray02),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .size(24.dp),
                onClick = {

            }) {
                Icon(painter = painterResource(id = R.drawable.i_arrows_chevron_backward),
                    contentDescription = null,
                    tint = colorResource(id = R.color.gray05)
                )

            }
        }
    }
}