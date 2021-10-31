package wupitch.android.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.common.Resource
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.databinding.ActivityOnboardingBinding
import wupitch.android.domain.model.OnboardingContent
import wupitch.android.presentation.ui.signup.SignupActivity

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(ActivityOnboardingBinding::inflate) {

    private lateinit var viewPagerAdapter: OnboardingVpAdapter
    private var onBoardingList = ArrayList<OnboardingContent>()
    private val viewModel : OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavBarColor()
        setStatusBar(R.color.gray04)
        setViewpager()
        binding.onboardingActivity = this
        binding.viewModel = viewModel

        viewModel.kakaoLoginState.observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    startActivity(Intent(this, SignupActivity::class.java))
                }
            }
        })

    }

    fun skipOnboarding () {
        binding.tablayoutOnboarding.getTabAt(3)?.select()
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

    fun signInWithKakao() {
//        val jwtPreferenceFlow: Flow<String?> = dataStore.data.map { preferences ->
//                preferences[JWT_PREFERENCE_KEY] ?: ""
//            }
//
//        val jwtToken: Flow<String?> = jwtPreferenceFlow
//        Log.d("{MainActivity.setKakaoLogin}", "$jwtToken")

//        if(jwtToken != null) {
//            //메인 화면으로 이동
//        }else {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) { //사용자 기기에 카카오톡 있는지 확인
            UserApiClient.instance.loginWithKakaoTalk(this, callback = kakaoCallback) //카카오톡 실행
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback) //웹뷰 실행.
        }
//        }

    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("{OnboardingActivity.getKakaoUserInfo}", error.message.toString())
            }
            else if (user != null) {
               Log.d("{OnboardingActivity.getKakaoUserInfo}", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n성별: ${user.kakaoAccount?.gender?.name}")
                val email = user.kakaoAccount?.email
                val genderType = user.kakaoAccount?.gender?.name
                val id = user.id
                val nickname = user.kakaoAccount?.profile?.nickname

                viewModel.postKakaoUserInfo(
                    KakaoLoginReq(
                    email = email!!,
                    genderType = genderType!!,
                    id =  id,
                    nickname = nickname!!
                )
                )
            }
        }
    }

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("{OnboardingActivity.kakaoCallback}", "login error! ${error.message}")

            when (error.toString()) {
                //flow 로 처리할 것
//                AuthErrorCause.AccessDenied.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_access_denied_toast_message
//                AuthErrorCause.InvalidClient.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_invalid_client_toast_message
//                AuthErrorCause.InvalidGrant.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_invalid_grant_toast_message
//                AuthErrorCause.InvalidRequest.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_invalid_request_toast_message
//                AuthErrorCause.InvalidScope.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_invalid_scope_toast_message
//                AuthErrorCause.Misconfigured.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_misconfigured_toast_message
//                AuthErrorCause.ServerError.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_server_error_toast_message
//                AuthErrorCause.Unauthorized.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_unauthorized_toast_message
//                AuthErrorCause.Unknown.toString() -> _kakaoErrorMessage.value = R.string.login_kakao_etc_toast_message
            }
        } else if (token != null) {
            Log.d("{OnboardingActivity.kakaoCallback}", "login success! ${token.accessToken}")
            //showLoadingDialog(this)
            //todo : viewmodel -> 서버에게 토큰 보내기.
//            startActivity(Intent(this, SignupActivity::class.java))
            getKakaoUserInfo()

        }
    }
}