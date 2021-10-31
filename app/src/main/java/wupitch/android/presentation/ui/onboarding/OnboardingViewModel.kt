package wupitch.android.presentation.ui.onboarding

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.domain.use_case.login.PostKakaoLoginUseCase
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val postKakaoLoginUseCase: PostKakaoLoginUseCase
) : ViewModel() {

    private var _kakaoErrorMessage = MutableLiveData<Int>()
    val kakaoErrorMessage : LiveData<Int> = _kakaoErrorMessage

    private var _kakaoLoginState = mutableStateOf(KakaoLoginState())
    val kakaoLoginState : State<KakaoLoginState> = _kakaoLoginState


    fun postKakaoLogin(kakaoUserInfo : KakaoLoginReq) {
        postKakaoLoginUseCase(kakaoUserInfo).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _kakaoLoginState.value = KakaoLoginState(isSuccess = true)
                }
                is Resource.Error -> {
                    _kakaoLoginState.value = KakaoLoginState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _kakaoLoginState.value = KakaoLoginState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}