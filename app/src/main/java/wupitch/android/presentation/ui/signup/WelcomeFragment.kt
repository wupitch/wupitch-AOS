package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.components.RoundBtn

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val nickname = viewModel.userNickname.value

                    ConstraintLayout {
                        val (title, subtitle, image, nextBtn) = createRefs()

                        Text(
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(parent.top, margin = 98.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            text = "$nickname " + stringResource(id = R.string.welcome_title),
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.main_black)
                        )

                        Text(
                            modifier = Modifier.constrainAs(subtitle) {
                                top.linkTo(title.bottom, margin = 4.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            text = stringResource(id = R.string.welcome_subtitle),
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.gray02)
                        )

                        Image(
                            modifier = Modifier.constrainAs(image){
                                top.linkTo(title.bottom)
                                bottom.linkTo(nextBtn.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            painter = painterResource(id = R.drawable.chrt_01),
                            contentDescription = "welcome character"
                        )

                        RoundBtn(
                            modifier = Modifier.constrainAs(nextBtn) {
                                start.linkTo(parent.start, margin = 20.dp)
                                end.linkTo(parent.end, margin = 20.dp)
                                bottom.linkTo(parent.bottom, margin = 32.dp)
                                width = Dimension.fillToConstraints
                            }.height(52.dp),
                            btnColor = R.color.main_orange,
                            textString = R.string.checkout_main,
                            fontSize = 16.sp
                        ) {
                            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
                        }
                    }


                }
            }
        }
    }
}