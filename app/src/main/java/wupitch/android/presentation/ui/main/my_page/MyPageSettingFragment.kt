package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import wupitch.android.presentation.ui.main.my_page.components.MyPageText

@AndroidEntryPoint
class MyPageSettingFragment : Fragment() {

    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean("isPushAgreed")?.let { value ->

            viewModel.setUserNotiState(value)

            Log.d("{MyPageSettingFragment.onCreate}", value.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val switchState = remember { viewModel.userNotiState }
                    val toggleState = remember { mutableStateOf(viewModel.userNotiState.value.data)}

                    if (switchState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            switchState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val unregisterState = remember { viewModel.unregisterState }
                    if (unregisterState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            unregisterState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (unregisterState.value.isSuccess) {
                        findNavController().navigate(R.id.action_myPageSettingFragment_to_loginFragment)
                    }

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        val (col, progressbar) = createRefs()


                        Column(Modifier.constrainAs(col) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {

                            TitleToolbar(
                                modifier = Modifier.fillMaxWidth(),
                                textString = R.string.settings
                            ) {
                                findNavController().navigateUp()
                            }

                            NotificationSettingLayout(toggleState) {
                                toggleState.value = it
                                viewModel.changeUserNotiState()
                            }

                            MyPageText(
                                modifier = Modifier.padding(start = 20.dp),
                                textString = stringResource(id = R.string.setting_change_pw)
                            ) {
                                findNavController().navigate(R.id.action_myPageSettingFragment_to_myPagePwFragment)
                            }
                            MyPageText(
                                modifier = Modifier.padding(start = 20.dp),
                                textString = stringResource(id = R.string.setting_logout)
                            ) {
                                viewModel.logoutUser()
                                findNavController().navigate(R.id.action_myPageSettingFragment_to_loginFragment)
                            }
                            MyPageText(
                                modifier = Modifier.padding(start = 20.dp),
                                textString = stringResource(id = R.string.setting_unregister)
                            ) {
                                viewModel.unregisterUser()
                            }
                        }

                        if (unregisterState.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun NotificationSettingLayout(
        switchState: MutableState<Boolean>,
        onCheckedChange: (Boolean) -> Unit
    ) {

        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {

            val (text, switch) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(vertical = 14.dp),
                text = stringResource(id = R.string.setting_notification),
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )

            Switch(
                modifier = Modifier
                    .constrainAs(switch) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end, margin = 25.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(50.dp, 30.dp),
                checked = switchState.value,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = colorResource(id = R.color.green02),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = colorResource(id = R.color.gray07)
                )
            )
        }

    }
}