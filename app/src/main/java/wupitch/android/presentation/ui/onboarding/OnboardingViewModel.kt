package wupitch.android.presentation.ui.onboarding

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.ResultState
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.data.remote.KakaoLoginRes
import wupitch.android.domain.repository.KakaoLoginRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val kakaoLoginRepository: KakaoLoginRepository
) : ViewModel() {

    private var _kakaoErrorMessage = MutableLiveData<Int>()
    val kakaoErrorMessage : LiveData<Int> = _kakaoErrorMessage

    private var _kakaoLoginState = mutableStateOf(ResultState<KakaoLoginRes>())
    val kakaoLoginState : State<ResultState<KakaoLoginRes>> = _kakaoLoginState


    fun postKakaoLogin(kakaoUserInfo : KakaoLoginReq) = viewModelScope.launch {
        _kakaoLoginState.value = ResultState(isLoading = true)
        try {
            //todo : try catch 가 필요한지.. 고민필요.
            val response = kakaoLoginRepository.postKakaoUserInfo(kakaoUserInfo)
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    if (result.isSuccess) {
                        //todo : success 이면 화면 이동. result 값 넘길 필요 없음....
                        _kakaoLoginState.value = ResultState(result = result)
                        Log.d("{OnboardingViewModel.postKakaoLogin}", _kakaoLoginState.value.result.toString())
                    }
                    else _kakaoLoginState.value = ResultState(error = "An unexpected error occured")
                }
            } else {
                _kakaoLoginState.value = ResultState(error = "An unexpected error occured")
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _kakaoLoginState.value = ResultState(error = "Network Failure")
                else -> _kakaoLoginState.value = ResultState(error = "Conversion Error")
            }
        }
    }

}