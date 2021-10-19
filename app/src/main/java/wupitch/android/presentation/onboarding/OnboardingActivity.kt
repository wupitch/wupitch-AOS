package wupitch.android.presentation.onboarding

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.databinding.ActivityOnboardingBinding
import wupitch.android.domain.model.OnboardingContent

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(ActivityOnboardingBinding::inflate) {

    private lateinit var viewPagerAdapter: OnboardingVpAdapter
    private var onBoardingList = ArrayList<OnboardingContent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBar()
        setViewpager()
        binding.onboardingActivity = this

    }

    fun skipOnboarding () {
        binding.tablayoutOnboarding.getTabAt(3)?.select()
    }

    private fun setStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray_onboarding_background)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    private fun setViewpager() {
        viewPagerAdapter = OnboardingVpAdapter(this, setOnboardingList())
        binding.viewpagerOnboarding.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(pageChangeListener)
        }
        TabLayoutMediator(binding.tablayoutOnboarding, binding.viewpagerOnboarding) { _, _ ->}.attach()
    }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if(position == 3) {
                binding.btnSkipOnboarding.visibility = View.GONE
                binding.ivKakaoLogin.visibility = View.VISIBLE
            }else {
                binding.btnSkipOnboarding.visibility = View.VISIBLE
                binding.ivKakaoLogin.visibility = View.GONE
            }
        }
    }

    private fun setOnboardingList() : ArrayList<OnboardingContent> {
        val onboarding1 = OnboardingContent(
            getString(R.string.onboarding_title1),
            getString(R.string.onboarding_subtitle1),
            R.drawable.img_onbd_01
        )
        val onboarding2 = OnboardingContent(
            getString(R.string.onboarding_title2),
            getString(R.string.onboarding_subtitle2),
            R.drawable.img_onbd_02
        )
        val onboarding3 = OnboardingContent(
            getString(R.string.onboarding_title3),
            getString(R.string.onboarding_subtitle3),
            R.drawable.img_onbd_03
        )
        val onboarding4 = OnboardingContent(
            getString(R.string.onboarding_title4),
            getString(R.string.onboarding_subtitle4),
            R.drawable.img_onbd_04
        )
        return onBoardingList.apply {
            add(onboarding1)
            add(onboarding2)
            add(onboarding3)
            add(onboarding4)
        }

    }
}