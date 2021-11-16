package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.StopWarningDialog
import wupitch.android.presentation.ui.signup.components.IdCardGuideLayout

class IdCardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if (stopSignupState.value) {
                        findNavController().navigate(R.id.action_profileFragment_to_onboardingFragment)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.warning_stop_signup)
                        )
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(bottom = 32.dp)
                    ) {
                        val (toolbar, contentCol, nextBtn) = createRefs()


                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            findNavController().navigateUp()
                        },
                            hasRightIcon = true,
                            onRightIconClick = {
                                dialogOpenState.value = true
                            }
                        )

                        Column(modifier = Modifier.constrainAs(contentCol) {
                            top.linkTo(toolbar.bottom, margin = 24.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                            end.linkTo(parent.end, margin = 20.dp)
                            width = Dimension.fillToConstraints
                        }) {
                            Text(
                                text = stringResource(id = R.string.certify_idcard),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                lineHeight = 32.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                                text = stringResource(id = R.string.certify_idcard_subtitle),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Normal,
                                color = colorResource(id = R.color.gray02),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
                            )
                            Image(
                                modifier = Modifier.height(228.dp),
                                painter = painterResource(id = R.drawable.img_idcard),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(28.dp))
                            IdCardGuideLayout()

                        }

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor =  R.color.main_black,
                            textString = R.string.take_idcard_photo,
                            fontSize = 16.sp
                        ) {
                            //todo open camera.
                        }


                    }
                }
            }
        }
    }
}