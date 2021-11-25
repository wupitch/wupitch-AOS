package wupitch.android.presentation.ui.main.my_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme

class MyActivityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        val (toolbar, homeList, fab) = createRefs()

                        MyActivityToolbar(
                            modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun MyActivityToolbar(
        modifier: Modifier,
    ) {
        ConstraintLayout(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (icon_notification, text) = createRefs()

                    Text(
                        text = stringResource(id = R.string.my_activity),
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
                            .constrainAs(icon_notification) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .size(24.dp),
                        onClick = {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                        }
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

}