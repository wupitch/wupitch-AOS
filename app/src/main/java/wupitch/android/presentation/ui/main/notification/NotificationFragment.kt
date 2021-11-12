package wupitch.android.presentation.ui.main.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
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
import wupitch.android.presentation.ui.components.TitleToolbar
import wupitch.android.presentation.ui.main.notification.components.NotificationList

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        TitleToolbar(
                            modifier = Modifier.fillMaxWidth(),
                            textString = R.string.notification
                        ) { findNavController().navigateUp() }

                        val notificationState = viewModel.notificationState.value

                        if(notificationState.isLoading){
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White), contentAlignment = Alignment.Center){
                                CircularProgressIndicator(color = colorResource(id = R.color.main_orange))
                            }
                        }

                        NotificationList(notificationList = notificationState.data, onClick = {
                            Log.d("{NotificationFragment.onCreateView}", "$it clicked!")
                        })


                        if (notificationState.data.isEmpty()) {
                            ConstraintLayout(Modifier.fillMaxSize()) {
                                val (image, text) = createRefs()

                                Image(
                                    modifier = Modifier.constrainAs(image) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                    painter = painterResource(id = R.drawable.ic_img_chrt_03),
                                    contentDescription = "no notification image"
                                )
                                Text(
                                    modifier = Modifier.constrainAs(text) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        top.linkTo(image.bottom, margin = 24.dp)
                                    },
                                    text = stringResource(id = R.string.no_notification),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(id = R.color.gray02),
                                    fontSize = 16.sp
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}