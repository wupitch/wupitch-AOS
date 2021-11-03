package wupitch.android.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.ActivityMainBinding
import wupitch.android.databinding.FragmentMainBinding
import wupitch.android.presentation.theme.BottomSheetShape
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.main.feed.FeedFragment
import wupitch.android.presentation.ui.main.home.HomeFragment
import wupitch.android.presentation.ui.main.home.components.CrewList
import wupitch.android.presentation.ui.main.impromptu.ImpromptuFragment
import wupitch.android.presentation.ui.main.my_activity.MyActivityFragment
import wupitch.android.presentation.ui.main.profile.ProfileFragment

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::bind,R.layout.fragment_main ) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBar(R.color.white)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_fag_nav_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
    }
}
