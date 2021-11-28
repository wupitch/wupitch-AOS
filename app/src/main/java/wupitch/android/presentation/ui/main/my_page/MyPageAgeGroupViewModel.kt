package wupitch.android.presentation.ui.main.my_page

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class MyPageAgeGroupViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {

    private var _userAge = mutableStateOf<Int?>(null)
    val userAge: State<Int?> = _userAge

    private var _updateState = mutableStateOf(BaseState())
    val updateState : State<BaseState> = _updateState

    fun setUserAge(ageCode: Int) {
        _userAge.value = ageCode
    }

    fun getUserAgeGroup() = viewModelScope.launch {
        val response = profileRepository.getUserAgeGroup()
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _userAge.value = if(res.result.ageIdx == null) null else res.result.ageIdx -1
                else _updateState.value = BaseState(error = res.message)
            }
        }else _updateState.value = BaseState(error = "연령대 조회에 실패했습니다.")
    }

    fun changeUserAgeGroup() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            ageNum = _userAge.value!! + 1
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