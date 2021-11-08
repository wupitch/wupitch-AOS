package wupitch.android.presentation.ui

import android.os.Bundle
import android.view.WindowManager
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


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_container_view) as NavHostFragment
        val navController = navHostFragment.navController



        //        supportFragmentManager.beginTransaction().replace(R.id.framelayout,
//        MainFragment()).commit()


    }
}
