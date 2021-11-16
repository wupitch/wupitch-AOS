package wupitch.android.presentation.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.domain.repository.CheckValidRepository
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val checkValidRepo: CheckValidRepository
) : ViewModel() {

    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    val userNickname: LiveData<String?> = _userNickname

    private var _isEmailValid = MutableLiveData<Boolean?>()
    val isEmailValid: LiveData<Boolean?> = _isEmailValid

    private var _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail

    private var _isPwValid = MutableLiveData<Boolean?>()
    val isPwValid: LiveData<Boolean?> = _isPwValid

    private var _userPw = MutableLiveData<String?>()
    val userPw: LiveData<String?> = _userPw

    fun checkNicknameValid(nickname: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkNicknameValid}", nickname)
        if (nickname.isEmpty()) {
            _isNicknameValid.value = null
            return@launch
        }

        val response = checkValidRepo.checkNicknameValidation(NicknameValidReq(nickname))
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

    fun checkEmailValid(email: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkEmailValid}", email)
        if (email.isEmpty()) {
            _isEmailValid.value = null
            return@launch
        }

        val response = checkValidRepo.checkEmailValidation(EmailValidReq(email))
        if (response.isSuccessful) {
            response.body()?.let { validRes ->
                if (validRes.isSuccess) {
                    _isEmailValid.value = true
                    _userEmail.value = email
                } else {
                    _isEmailValid.value = false
                }
            }
        } else _isEmailValid.value = false
    }


    fun checkPwValid(pw: String) {

        Log.d("{SignupViewModel.isPwValid}", pw)
        if (pw.isEmpty()) {
            _isPwValid.value = null
            return
        }

        val isPwValid = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$".toRegex())
        Log.d("{SignupViewModel.isPwValid}", isPwValid.toString())

        if (isPwValid) {
            _isPwValid.value = true
            _userPw.value = pw
        } else {
            _isPwValid.value = false
        }

    }
}