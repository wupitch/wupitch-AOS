package wupitch.android.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.databinding.ActivitySplashBinding
import wupitch.android.presentation.onboarding.OnboardingActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColor()

        //자동 로그인 확인.
        lifecycleScope.launch {
            //임시 delay
            delay(1500L)
            withContext(Dispatchers.Main){
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            }
        }
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_orange)
    }
}