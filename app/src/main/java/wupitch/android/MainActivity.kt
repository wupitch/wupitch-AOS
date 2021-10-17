package wupitch.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import wupitch.android.WupitchApplication.Companion.dataStore
import wupitch.android.common.BaseActivity
import wupitch.android.common.Constants.JWT_PREFERENCE_KEY
import wupitch.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("{MainActivity.onCreate}", keyHash.toString())

        binding.button.setOnClickListener {
            setKakaoLogin()
        }

    }

    private fun setKakaoLogin() {
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

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when (error.toString()) {
                AuthErrorCause.AccessDenied.toString() -> showCustomToast(getString(R.string.login_kakao_access_denied_toast_message))
                AuthErrorCause.InvalidClient.toString() -> showCustomToast(getString(R.string.login_kakao_invalid_client_toast_message))
                AuthErrorCause.InvalidGrant.toString() -> showCustomToast(getString(R.string.login_kakao_invalid_grant_toast_message))
                AuthErrorCause.InvalidRequest.toString() -> showCustomToast(getString(R.string.login_kakao_invalid_request_toast_message))
                AuthErrorCause.InvalidScope.toString() -> showCustomToast(getString(R.string.login_kakao_invalid_scope_toast_message))
                AuthErrorCause.Misconfigured.toString() -> showCustomToast(getString(R.string.login_kakao_misconfigured_toast_message))
                AuthErrorCause.ServerError.toString() -> showCustomToast(getString(R.string.login_kakao_server_error_toast_message))
                AuthErrorCause.Unauthorized.toString() -> showCustomToast(getString(R.string.login_kakao_unauthorized_toast_message))
                AuthErrorCause.Unknown.toString() -> showCustomToast(getString(R.string.login_kakao_etc_toast_message))
            }
        } else if (token != null) {
            Log.d("{MainActivity.kakaoCallback}", "login success! ${token.accessToken}")
            //showLoadingDialog(this)
            //서버에게 토큰 보내기.
        }
    }
}