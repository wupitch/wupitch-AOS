package wupitch.android.presentation.onboarding

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.WupitchApplication
import wupitch.android.common.Resource
import wupitch.android.data.repository.KakaoLoginReq
import wupitch.android.data.repository.TestRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val repository: TestRepository
) : ViewModel() {

    private var _kakaoErrorMessage = MutableLiveData<Int>()
    val kakaoErrorMessage : LiveData<Int> = _kakaoErrorMessage

    private var _kakaoLoginState = MutableLiveData<Resource<String>>()
    val kakaoLoginState : LiveData<Resource<String>> = _kakaoLoginState

    fun postKakaoUserInfo(kakaoUserInfo : KakaoLoginReq) = viewModelScope.launch {
        val response = repository.postKakaoUserInfo(kakaoUserInfo)
        if(response.isSuccessful) {
            response.body()?.let {
                Log.d("{OnboardingViewModel.postKakaoUserInfo}", it.result.toString())
            }
        }
    }

}