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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import wupitch.android.R
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.main_orange)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "spash screen logo")

                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {

            delay(1000L)

            val jwt = viewModel.readJwt()
            Log.d("{SplashFragment.onViewCreated}", jwt.toString())

            withContext(Dispatchers.Main){
                if(jwt != null) findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                else findNavController().navigate(R.id.action_splashFragment_to_serviceAgreementFragment)
            }
        }

    }

}