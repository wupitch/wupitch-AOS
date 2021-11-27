package wupitch.android.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentMainBinding
import wupitch.android.fcm.FcmViewModel

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::bind,R.layout.fragment_main)
{
    private var tabId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("tabId")?.let { id ->
            if(id != -1) tabId = id
        }
    }

    private lateinit var navController: NavController
    private val viewModel : FcmViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBar(R.color.white)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_fag_nav_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
        if(tabId != -1){
            binding.bottomNavView.selectedItemId = tabId
        }

        initFcm()

    }

    private fun initFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("{FcmService.onCreate}", task.exception.toString())
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("{MainFragment.initFcm}", token.toString())
            viewModel.registerToken(token.toString())
        })
    }
}
