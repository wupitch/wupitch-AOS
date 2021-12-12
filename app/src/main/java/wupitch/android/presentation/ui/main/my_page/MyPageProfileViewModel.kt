package wupitch.android.presentation.ui.main.my_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.UpdateUserInfoReq
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

    private var _loadState = mutableStateOf(BaseState())
    val loadState : State<BaseState> = _loadState

    fun initLoadState () {
        _loadState.value = BaseState()
    }

    fun getUserInfo() = viewModelScope.launch {
        _loadState.value = BaseState(isLoading = true)

        val response = profileRepository.getUserInfo()
        if(response.isSuccessful){
            response.body()?.let { infoRes ->
                if(infoRes.isSuccess) {
                    _userNickname.value = infoRes.result.nickname
                    _userIntroduce.value = infoRes.result.introduce
                    _loadState.value = BaseState(isSuccess = true)
                }
                else   _loadState.value = BaseState(error = infoRes.message)
            }
        }else _loadState.value = BaseState(error = "유저 정보 조회를 실패했습니다.")
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
                _isNicknameValid.value = validRes.isSuccess
            }
        } else _isNicknameValid.value = false

    }

    fun setUserNickname(nickname : String){
        _userNickname.value = nickname
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