package wupitch.android.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::bind,R.layout.fragment_main ){

    private var tabId = 0
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("tab_id")?.let { id ->
            tabId = id
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBar(R.color.white)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_fag_nav_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
//        binding.bottomNavView.setOnItemSelectedListener(this)

        binding.bottomNavView.selectedItemId = when(tabId){
            0 -> R.id.homeFragment
            1-> R.id.impromptuFragment
            2 -> R.id.myActivityFragment
            3 -> R.id.feedFragment
            else -> R.id.myPageFragment
        }

    }


//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        navController.navigate(item.itemId)
////        Navigation.findNavController(requireContext())
////        findNavController().navigate(item.itemId)
//        return true
//
//    }
}
