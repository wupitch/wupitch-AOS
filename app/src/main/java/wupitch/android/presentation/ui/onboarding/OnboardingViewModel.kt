package wupitch.android.presentation.ui.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.common.Resource
import wupitch.android.domain.model.KakaoLoginReq
import wupitch.android.data.remote.dto.KakaoLoginRes
import wupitch.android.domain.model.LoginReq
import wupitch.android.domain.repository.KakaoLoginRepository
import wupitch.android.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val loginRepository: LoginRepository,
    private val kakaoLoginRepository: KakaoLoginRepository,
) : ViewModel() {


    private var _kakaoLoginLiveData = MutableLiveData<Resource<KakaoLoginRes>>()
    val kakaoLoginLiveData: LiveData<Resource<KakaoLoginRes>> = _kakaoLoginLiveData

    var userEmail = mutableStateOf("")
    var userPw = mutableStateOf("")

    private var _loginState = mutableStateOf(LoginState())
    val loginState : State<LoginState> = _loginState

    fun tryLogin() = viewModelScope.launch {
        _loginState.value = LoginState(isLoading = true)

        val loginReq = LoginReq(
            email = userEmail.value,
            password = userPw.value
        )
        val response = loginRepository.login(loginReq)

        if(response.isSuccessful) {
            response.body()?.let { loginRes ->
                if(loginRes.isSuccess) {
                    _loginState.value = LoginState(isSuccess = true)
                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = loginRes.result.jwt
                        settings[Constants.USER_ID] = loginRes.result.accountId
                        settings[Constants.USER_EMAIL] = loginRes.result.email
                    }
                }
                else _loginState.value = LoginState(error = loginRes.message)
            }
        }else{
            _loginState.value = LoginState(error = "로그인에 실패했습니다.")
        }
    }

    fun postKakaoLogin(kakaoUserInfo: KakaoLoginReq) = viewModelScope.launch {
        _kakaoLoginLiveData.value = Resource.Loading<KakaoLoginRes>()

        val response = kakaoLoginRepository.postKakaoUserInfo(kakaoUserInfo)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.isSuccess) {
                    _kakaoLoginLiveData.value = Resource.Success<KakaoLoginRes>(data = result)

                    Log.d(
                        "{OnboardingViewModel.postKakaoLogin}",
                        "jwt : ${result.result.jwt} userId : ${result.result.accountId}"
                    )

                    //jwt & userId 저장.
                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = result.result.jwt
                        settings[Constants.USER_ID] = result.result.accountId
                    }
                } else _kakaoLoginLiveData.value =
                    Resource.Error<KakaoLoginRes>(message = "An unexpected error occured")
            }
        } else {
            _kakaoLoginLiveData.value = Resource.Error<KakaoLoginRes>(message = "An unexpected error occured")
        }
    }

}