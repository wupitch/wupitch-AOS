package wupitch.android.fcm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.domain.repository.FcmRepository
import javax.inject.Inject

@HiltViewModel
class FcmViewModel @Inject constructor(
    private val fcmRepository: FcmRepository
) : ViewModel() {

    private var _registerTokenState = mutableStateOf(BaseState())
    val registerTokenState : State<BaseState> = _registerTokenState

    fun registerToken(token : String) = viewModelScope.launch {
        val response = fcmRepository.postToken("테스트 내용", token, "테스트 제목")
        if(response.isSuccessful){
            response.body()?.let {
                if(!it.isSuccess) _registerTokenState.value = BaseState(error = it.message)
            }
        }else  _registerTokenState.value = BaseState(error = "토큰 등록에 실패했습니다.")
    }


}