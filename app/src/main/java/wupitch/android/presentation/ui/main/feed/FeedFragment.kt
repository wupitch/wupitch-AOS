package wupitch.android.presentation.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.NotiToolbar

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    )
                    {
                        NotiToolbar(
                            modifier = Modifier.fillMaxWidth(),
                            textString = stringResource(id = R.string.feed)
                        ) {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                        }

                    }
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val (chrt, text) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

                        Image(
                            modifier = Modifier
                                .constrainAs(chrt) {
                                    bottom.linkTo(text.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .size(130.dp, 210.dp),
                            painter = painterResource(id = R.drawable.img_chrt_02),
                            contentDescription = null)

                        Text(
                            modifier = Modifier
                                .constrainAs(text) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(guildLine)
                                }
                                .padding(top = 24.dp),
                            text = stringResource(R.string.preparing),
                            color = colorResource(id = R.color.gray02),
                            fontFamily = Roboto,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}