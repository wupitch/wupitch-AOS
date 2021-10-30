package wupitch.android.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
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
        binding.bottomNavView.apply {
            setOnItemSelectedListener(itemSelectedListener)
            selectedItemId = R.id.menu_home
        }

    }

    private val itemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> loadFragment(HomeFragment())
                R.id.menu_impromptu -> loadFragment(ImpromptuFragment())
                R.id.menu_feed -> loadFragment(FeedFragment())
                R.id.menu_myactivity -> loadFragment(MyActivityFragment())
                else -> loadFragment(ProfileFragment())
            }
            true
        }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_main, fragment)
            .commit()
    }
}
