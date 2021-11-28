package wupitch.android.presentation.ui.main.my_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class MyPageContactViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var userPhoneNum = mutableStateOf("")

    fun setUserPhoneNum(phoneNum: String) {
        userPhoneNum.value = phoneNum
    }

    private var _updateState = mutableStateOf(BaseState())
    val updateState : State<BaseState> = _updateState


    fun getUserPhoneNum() = viewModelScope.launch {
        val response = profileRepository.getUserPhoneNum()
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) userPhoneNum.value = res.result.phoneNumber ?: "000-0000-0000"
                else _updateState.value = BaseState(error = res.message)
            }
        } else _updateState.value = BaseState(error = "핸드폰 번호 조회에 실패했습니다.")
    }

    fun changeUserPhoneNum() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            phoneNumber = userPhoneNum.value
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "정보 변경에 실패했습니다.")
    }
}