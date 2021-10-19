package wupitch.android.presentation.onboarding

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import wupitch.android.R
import wupitch.android.WupitchApplication
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val application : WupitchApplication
)  : AndroidViewModel(application) {

    private var _kakaoErrorMessage = MutableStateFlow<Int>(0)
    val kakaoErrorMessage : StateFlow<Int> = _kakaoErrorMessage

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
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(application)) { //사용자 기기에 카카오톡 있는지 확인
            UserApiClient.instance.loginWithKakaoTalk(application, callback = kakaoCallback) //카카오톡 실행
        } else {
            UserApiClient.instance.loginWithKakaoAccount(application, callback = kakaoCallback) //웹뷰 실행.
        }
//        }
    }

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when (error.toString()) {
                //flow 로 처리할 것
                AuthErrorCause.AccessDenied.toString() -> _kakaoErrorMessage = R.string.login_kakao_access_denied_toast_message
                AuthErrorCause.InvalidClient.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_client_toast_message
                AuthErrorCause.InvalidGrant.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_grant_toast_message
                AuthErrorCause.InvalidRequest.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_request_toast_message)
                AuthErrorCause.InvalidScope.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_scope_toast_message)
                AuthErrorCause.Misconfigured.toString() -> _kakaoErrorMessage = R.string.login_kakao_misconfigured_toast_message)
                AuthErrorCause.ServerError.toString() -> _kakaoErrorMessage = R.string.login_kakao_server_error_toast_message)
                AuthErrorCause.Unauthorized.toString() -> _kakaoErrorMessage = R.string.login_kakao_unauthorized_toast_message)
                AuthErrorCause.Unknown.toString() -> _kakaoErrorMessage = R.string.login_kakao_etc_toast_message)
            }
        } else if (token != null) {
            Log.d("{MainActivity.kakaoCallback}", "login success! ${token.accessToken}")
            //showLoadingDialog(this)
            //서버에게 토큰 보내기.
        }
    }
}