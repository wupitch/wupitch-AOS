package wupitch.android.presentation.ui.signup

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.domain.model.SignupReq
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.SignupRepository
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val signupRepository : SignupRepository,
    @ApplicationContext val context : Context
) : ViewModel() {

    private var _userPushAgreed = MutableLiveData<Boolean>()
    val userPushAgreed : LiveData<Boolean> = _userPushAgreed

    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    private var _userIntroduce = MutableLiveData<String>()

    private var _isEmailValid = MutableLiveData<Boolean?>()
    val isEmailValid: LiveData<Boolean?> = _isEmailValid

    private var _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail

    private var _isPwValid = MutableLiveData<Boolean?>()
    val isPwValid: LiveData<Boolean?> = _isPwValid

    private var _userPw = MutableLiveData<String?>()
    val userPw: LiveData<String?> = _userPw

    private var _signupState = MutableLiveData<Boolean>()
    val signupState : LiveData<Boolean> = _signupState

    fun setUserPushAgreement (userAgreed : Boolean) {
        _userPushAgreed.value = userAgreed
    }

    fun checkNicknameValid(nickname: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkNicknameValid}", nickname)
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

    fun setUserIntroduce (introduce : String) {
        _userIntroduce.value = introduce
    }

    fun checkEmailValid(email: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkEmailValid}", email)
        if (email.isEmpty()) {
            _isEmailValid.value = null
            return@launch
        }

        val response = checkValidRepository.checkEmailValidation(EmailValidReq(email))
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

    fun postSignup() = viewModelScope.launch {
        val signupReq = SignupReq(
            email = _userEmail.value!!,
            introduce = _userIntroduce.value!!,
            isPushAgree = _userPushAgreed.value!!,
            nickname = _userNickname.value!!,
            password = _userPw.value!!
        )
        val response = signupRepository.signup(signupReq)

        if(response.isSuccessful) {
            response.body()?.let { signupRes ->
                if(signupRes.isSuccess) {

                    _signupState.value = true

                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = signupRes.result.jwt
                        settings[Constants.USER_ID] = signupRes.result.accountId
                        settings[Constants.USER_NICKNAME] = signupRes.result.nickname
                    }
                }else {
                    _signupState.value = false

                }
            }
        } else  _signupState.value = false

    }


}