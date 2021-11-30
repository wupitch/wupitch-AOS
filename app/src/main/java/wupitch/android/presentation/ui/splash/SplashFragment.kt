package wupitch.android.presentation.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.presentation.theme.SplashTheme

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel : SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                SplashTheme {

                    val editable =  remember { mutableStateOf(true) }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(id = R.color.main_orange)),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedVisibility(
                                visible = editable.value,
//                                enter = slideInVertically(
//                                    // Slide in from 40 dp from the top.
////                                    initialOffsetY = { with(density) { -40.dp.roundToPx() } }
//                                ) + expandVertically(
//                                    // Expand from the top.
//                                    expandFrom = Alignment.Top
//                                ) + fadeIn(
//                                    // Fade in with the initial alpha of 0.3f.
//                                    initialAlpha = 0.3f
//                                ),
//                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.ic_logo),
                                    contentDescription = "spash screen logo"
                                )
                            }
                        }


                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {




            val jwt = viewModel.readJwt()
            delay(1300L)

//            Log.d("{SplashFragment.onViewCreated}", jwt.toString())

            withContext(Dispatchers.Main){

//                if(jwt != null && jwt.isNotEmpty())
                  //findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
//                else
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)


                //development 용도.
                //findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_createCrewImageFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_createCrewInfoFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_createCrewScheduleFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_idCardFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_createCrewFeeFragment)
                //findNavController().navigate(R.id.action_splashFragment_to_createCrewFeeFragment)

            }
        }

    }

}