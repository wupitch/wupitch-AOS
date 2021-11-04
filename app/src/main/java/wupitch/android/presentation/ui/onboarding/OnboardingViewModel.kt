package wupitch.android.presentation.ui.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import wupitch.android.WupitchApplication.Companion.dataStore
import wupitch.android.common.Constants
import wupitch.android.common.Resource
import wupitch.android.common.ResultState
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.data.remote.KakaoLoginRes
import wupitch.android.domain.repository.KakaoLoginRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val kakaoLoginRepository: KakaoLoginRepository
) : ViewModel() {


    private var _kakaoLoginLiveData = MutableLiveData<Resource<KakaoLoginRes>>()
    val kakaoLoginLiveData : LiveData<Resource<KakaoLoginRes>> = _kakaoLoginLiveData

    fun postKakaoLogin(kakaoUserInfo : KakaoLoginReq) = viewModelScope.launch {
        _kakaoLoginLiveData.value = Resource.Loading<KakaoLoginRes>()

        val response = kakaoLoginRepository.postKakaoUserInfo(kakaoUserInfo)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.isSuccess) {
                    _kakaoLoginLiveData.value = Resource.Success<KakaoLoginRes>(data = result)

                    Log.d("{OnboardingViewModel.postKakaoLogin}",
                        "jwt : ${result.result.jwt} userId : ${result.result.accountId}")

                   //jwt & userId 저장.
                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = result.result.jwt
                        settings[Constants.USER_ID] = result.result.accountId
                    }
                }
                else _kakaoLoginLiveData.value = Resource.Error<KakaoLoginRes>(message = "An unexpected error occured")
            }
        } else {
            _kakaoLoginLiveData.value = Resource.Error<KakaoLoginRes>(message = "An unexpected error occured")
        }
    }

}