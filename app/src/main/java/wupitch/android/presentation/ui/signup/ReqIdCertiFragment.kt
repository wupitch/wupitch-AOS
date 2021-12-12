package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.RoundBtn

@AndroidEntryPoint
class ReqIdCertiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    ConstraintLayout(Modifier.background(Color.White).padding(horizontal = 20.dp)) {
                        val (title, subtitle, image, btn) = createRefs()

                        Text(
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(parent.top, margin = 80.dp)
                                start.linkTo(parent.start)
                            },
                            text = stringResource(id = R.string.requested_id_certification),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 32.sp
                        )
                        Text(
                            modifier = Modifier.constrainAs(subtitle) {
                                top.linkTo(title.bottom, margin = 8.dp)
                                start.linkTo(parent.start)
                            },
                            text = stringResource(id = R.string.id_certification_process_guide),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.gray02),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 22.sp
                        )
                        Image(
                            modifier = Modifier.height(280.dp).constrainAs(image){
                                top.linkTo(title.bottom)
                                bottom.linkTo(btn.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            painter = painterResource(id = R.drawable.chrt_01),
                            contentDescription = null
                        )

                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(btn) {
                                    bottom.linkTo(parent.bottom, 32.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor = R.color.main_orange,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            findNavController().navigate(R.id.action_reqIdCertiFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }
}