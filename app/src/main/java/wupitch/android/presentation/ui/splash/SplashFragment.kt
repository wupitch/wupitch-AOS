package wupitch.android.presentation.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wupitch.android.R
import wupitch.android.presentation.theme.SplashTheme

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SplashTheme {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.main_orange)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(159.dp, 138.dp),
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "splash screen logo"
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            //development
//            val jwt = viewModel.readJwt()
//            Log.d("{SplashFragment.onViewCreated}", jwt.toString())
            delay(700L)

            withContext(Dispatchers.Main) {
                if (viewModel.checkIsUserConfirmed()) {
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                }
                else findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }
        }
    }
}