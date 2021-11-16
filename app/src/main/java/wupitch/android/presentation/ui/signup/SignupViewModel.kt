package wupitch.android.presentation.ui.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.ValidReq
import wupitch.android.domain.repository.CheckValidRepository
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val checkValidRepo : CheckValidRepository
) : ViewModel() {

    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid : LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    val userNickname : LiveData<String?> = _userNickname

    fun checkNicknameValid (nickname : String?) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkNicknameValid}", nickname.toString())
        if(nickname == null) _isNicknameValid.value = null
        nickname?.let {
            val response = checkValidRepo.checkValidation(ValidReq(nickname))
            if(response.isSuccessful) {
                response.body()?.let { validRes ->
                    if(validRes.isSuccess) {
                        _isNicknameValid.value = true
                        _userNickname.value = nickname
                    }else {
                        _isNicknameValid.value = false
                    }
                }
            }else _isNicknameValid.value = false
        }
    }
}