package wupitch.android.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import wupitch.android.presentation.ui.main.home.HomeViewModel

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::bind,R.layout.fragment_main),
        NavigationBarView.OnItemSelectedListener
{
    private var tabId = -1
    private var notEnoughInfo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("tabId")?.let { id ->
            if(id != -1) tabId = id
        }
        arguments?.getBoolean("notEnoughInfo")?.let { notEnoughInfo = it }
    }

    private lateinit var navController: NavController



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBar(R.color.white)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_fag_nav_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnItemSelectedListener(this)
        if(tabId != -1){
            binding.bottomNavView.selectedItemId = tabId
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val arguments = argumentsByFragmentId[fragmentId] // custom mutableMapOf<Int, Bundle?>() with arguments

        when(item.itemId){
            R.id.myPageFragment -> {
                val bundle2 = Bundle().apply { putBoolean("notEnoughInfo", notEnoughInfo)}
                navController.navigate(item.itemId, bundle2)
            }
           else -> navController.navigate(item.itemId)

        }
        return true
    }
}
