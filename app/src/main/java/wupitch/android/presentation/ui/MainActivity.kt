package wupitch.android.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.common.BaseState
import wupitch.android.databinding.ActivityMainBinding
import wupitch.android.domain.repository.FcmRepository
import wupitch.android.fcm.FcmViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_container_view) as NavHostFragment
        val navController = navHostFragment.navController


    }
}
