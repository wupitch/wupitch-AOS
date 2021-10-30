package wupitch.android.presentation.ui.onboarding

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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