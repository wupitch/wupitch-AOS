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
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.data.remote.dto.toResult
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class MyPageProfileViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    /*
    * user info
    * */

    private var _isNicknameValid = mutableStateOf<Boolean?>(null)
    val isNicknameValid: State<Boolean?> = _isNicknameValid

    private var _userNickname = mutableStateOf<String>("")
    val userNickname: State<String> = _userNickname

    private var _userIntroduce =  mutableStateOf<String>("")
    val userIntroduce: State<String> = _userIntroduce


    fun getUserInfo() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)

        val response = profileRepository.getUserInfo()
        if(response.isSuccessful){
            response.body()?.let { infoRes ->
                if(infoRes.isSuccess) {
                    _userNickname.value = infoRes.result.nickname
                    _userIntroduce.value = infoRes.result.introduce
                }
                else   _updateState.value = BaseState(error = infoRes.message)
            }
        }else  _updateState.value = BaseState(error = "유저 정보 조회를 실패했습니다.")
    }

    private var _updateState = mutableStateOf(BaseState())
    val updateState : State<BaseState> = _updateState





    fun checkNicknameValid(nickname: String) = viewModelScope.launch {
        if (nickname.isEmpty()) {
            _isNicknameValid.value = null
            return@launch
        }

        val response = checkValidRepository.checkNicknameValidation(NicknameValidReq(nickname))
        if (response.isSuccessful) {
            response.body()?.let { validRes ->
                if (validRes.isSuccess) {
                    _isNicknameValid.value = true
                    _userNickname.value = nickname
                } else {
                    _isNicknameValid.value = false
                }
            }
        } else _isNicknameValid.value = false

    }

    fun setUserIntroduce(introduce: String) {
        _userIntroduce.value = introduce
    }

    fun changeUserNicknameOrIntro() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            nickname =  _userNickname.value,
            introduce = _userIntroduce.value
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