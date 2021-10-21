package wupitch.android.presentation.onboarding

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import wupitch.android.common.Resource
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _kakaoErrorMessage = MutableStateFlow<Int>(0)
    val kakaoErrorMessage : StateFlow<Int> = _kakaoErrorMessage

    private var _kakaoLoginState = MutableLiveData<Resource<String>>()
    val kakaoLoginState : LiveData<Resource<String>> = _kakaoLoginState

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
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) { //사용자 기기에 카카오톡 있는지 확인
            UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback) //카카오톡 실행
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback) //웹뷰 실행.
        }
//        }
    }

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("{MainActivity.kakaoCallback}", "login success! ${error.message}")

            when (error.toString()) {
                //flow 로 처리할 것
//                AuthErrorCause.AccessDenied.toString() -> _kakaoErrorMessage = R.string.login_kakao_access_denied_toast_message
//                AuthErrorCause.InvalidClient.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_client_toast_message
//                AuthErrorCause.InvalidGrant.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_grant_toast_message
//                AuthErrorCause.InvalidRequest.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_request_toast_message)
//                AuthErrorCause.InvalidScope.toString() -> _kakaoErrorMessage = R.string.login_kakao_invalid_scope_toast_message)
//                AuthErrorCause.Misconfigured.toString() -> _kakaoErrorMessage = R.string.login_kakao_misconfigured_toast_message)
//                AuthErrorCause.ServerError.toString() -> _kakaoErrorMessage = R.string.login_kakao_server_error_toast_message)
//                AuthErrorCause.Unauthorized.toString() -> _kakaoErrorMessage = R.string.login_kakao_unauthorized_toast_message)
//                AuthErrorCause.Unknown.toString() -> _kakaoErrorMessage = R.string.login_kakao_etc_toast_message)
            }
        } else if (token != null) {
            Log.d("{MainActivity.kakaoCallback}", "login success! ${token.accessToken}")
            //showLoadingDialog(this)
            //서버에게 토큰 보내기.
            _kakaoLoginState.value = Resource.Success<String>("success")
        }
    }
}