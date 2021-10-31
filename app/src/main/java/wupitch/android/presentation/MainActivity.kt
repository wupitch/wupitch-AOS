package wupitch.android.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.databinding.ActivityMainBinding
import wupitch.android.presentation.ui.main.feed.FeedFragment
import wupitch.android.presentation.ui.main.home.HomeFragment
import wupitch.android.presentation.ui.main.impromptu.ImpromptuFragment
import wupitch.android.presentation.ui.main.my_activity.MyActivityFragment
import wupitch.android.presentation.ui.main.profile.ProfileFragment

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavBarColor()
        setStatusBar(R.color.white)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)


    }
}
